package tech.hongjian.oa.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.BpmnAutoLayout;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.Process;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.flowable.engine.RepositoryService;
import org.flowable.validation.ProcessValidator;
import org.flowable.validation.ProcessValidatorFactory;
import org.flowable.validation.ValidationError;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hongjian.oa.entity.Model;
import tech.hongjian.oa.entity.User;
import tech.hongjian.oa.entity.enums.ModelType;
import tech.hongjian.oa.exception.CommonServiceException;
import tech.hongjian.oa.mapper.ModelMapper;
import tech.hongjian.oa.service.ModelImageService;
import tech.hongjian.oa.service.ModelService;
import tech.hongjian.oa.util.CommonUtil;
import tech.hongjian.oa.util.WebUtil;
import tech.hongjian.oa.util.XmlUtil;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xiahongjian
 * @since 2021-03-20
 */
@Slf4j
@Service
public class ModelServiceImpl extends ServiceImpl<ModelMapper, Model> implements ModelService {
    protected BpmnXMLConverter bpmnXmlConverter = new BpmnXMLConverter();
    protected BpmnJsonConverter bpmnJsonConverter = new BpmnJsonConverter();
    protected ObjectMapper objectMapper = new ObjectMapper();

    @Setter(onMethod_ = {@Autowired})
    private RepositoryService repositoryService;

    @Setter(onMethod_ = {@Autowired})
    private ModelImageService modelImageService;

    @Override
    public boolean modelExisted(String modelId, ModelType modelType) {
        return lambdaQuery().eq(Model::getModelId, modelId).eq(Model::getModelType, modelType).count() > 0;
    }

    @Override
    public Model createModel(Model model, Integer createdBy) {
        Model entity = new Model();
        BeanUtils.copyProperties(model, entity);
        CommonUtil.setEntityDefault(entity, createdBy);
        entity.setVersion(1);

        if (StringUtils.isNotEmpty(model.getModelEditorJson())) {
            if (entity.getModelType() == ModelType.BPMN) {

                // Thumbnail
                byte[] thumbnail = modelImageService.generateThumbnailImage(entity);
                if (thumbnail != null) {
                    entity.setThumbnail(thumbnail);
                }

                save(entity);

                // Relations
//                handleBpmnProcessFormModelRelations(model, jsonNode);
//                handleBpmnProcessDecisionTaskModelRelations(model, jsonNode);

            }
        }
        return entity;
    }

    @Override
    public Model updateModel(Model model, Integer updatedBy) {
        Model entity = new Model();
        BeanUtils.copyProperties(model, entity);
        CommonUtil.setUpdateDefault(entity, updatedBy);
        updateById(entity);
        return entity;
    }

    @Override
    public Model updateModel(Integer id, InputStream inputStream, String comment) {
        Model byId = getById(id);
        if (inputStream != null) {
            Model model = parseXml(inputStream);
            BeanUtils.copyProperties(model, byId);
        }
        byId.setUpdatedBy(WebUtil.currentUser().getId());
        byId.setUpdatedAt(LocalDateTime.now());
        byId.setModelComment(comment);
        if (!updateById(byId)) {
            throw new CommonServiceException("更新失败。");
        }
        return byId;
    }

    @Override
    public void deleteModel(Integer id) {
        Model byId = getById(id);
        if (byId == null) {
            throw new CommonServiceException("ID为" + id + "的模板不存在。");
        }
        removeById(id);
    }

    private Model parseXml(InputStream inputStream) {
        XMLInputFactory xif = XmlUtil.createSafeXmlInputFactory();
        try {
            InputStreamReader xmlIn = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            XMLStreamReader xtr = xif.createXMLStreamReader(xmlIn);
            BpmnModel bpmnModel = bpmnXmlConverter.convertToBpmnModel(xtr);

            //模板验证
            ProcessValidator validator = new ProcessValidatorFactory().createDefaultProcessValidator();
            List<ValidationError> errors = validator.validate(bpmnModel);
            if (CollectionUtils.isNotEmpty(errors)) {
                StringBuffer es = new StringBuffer();
                errors.forEach(ve -> es.append(ve.toString()).append("\n"));
                throw new CommonServiceException("模板验证失败，原因: " + es.toString());
            }
            if (CollectionUtils.isEmpty(bpmnModel.getProcesses())) {
                throw new CommonServiceException("未找到流程定义。");
            }
            if (bpmnModel.getLocationMap().size() == 0) {
                BpmnAutoLayout bpmnLayout = new BpmnAutoLayout(bpmnModel);
                bpmnLayout.execute();
            }
            ObjectNode modelNode = bpmnJsonConverter.convertToJson(bpmnModel);
            Process process = bpmnModel.getMainProcess();
            String name = process.getId();
            if (StringUtils.isNotBlank(process.getName())) {
                name = process.getName();
            }
            String description = process.getDocumentation();
            //查询是否已经存在流程模板
            Model newModel = new Model();
            newModel.setName(name);
            newModel.setModelId(process.getId());
            newModel.setModelType(ModelType.BPMN);
            newModel.setDescription(description);
            newModel.setModelEditorJson(modelNode.toString());

            return newModel;
        } catch (XMLStreamException e) {
            log.warn("导入流程模板失败，原因：{}", e.getMessage(), e);
            throw new CommonServiceException("导入流程模板失败，原因：" + e.getMessage());
        }
    }

    @Override
    public Model importModel(InputStream inputStream, String comment) {
        Model model = parseXml(inputStream);
        //查询是否已经存在流程模板
        if (modelExisted(model.getModelId(), ModelType.BPMN)) {
            throw new CommonServiceException("流程ID为: " + model.getModelId() +
                    "的流程模板已经存在。");
        }
        User currentUser = WebUtil.currentUser();
        return createModel(model, currentUser.getId());
    }

    @Override
    public BpmnModel getBpmnModel(Model model) {
        try {
            JsonNode editorJsonNode = objectMapper.readTree(model.getModelEditorJson());
            return bpmnJsonConverter.convertToBpmnModel(editorJsonNode);
        } catch (JsonProcessingException e) {
            throw new CommonServiceException("转化成BPMN Model失败，{}" + e.getMessage(), e);
        }
    }

    @Override
    public Model getModel(Integer id) {
        Model byId = getById(id);
        if (byId == null) {
            throw new CommonServiceException("ID为" + id + "的流程模板不存在。");
        }
        return byId;
    }

    @Override
    public byte[] getXmlData(Integer id) {
        Model model = getModel(id);
        return getXmlData(model);
    }

    @Override
    public byte[] getXmlData(Model model) {
        BpmnModel bpmnModel = getBpmnModel(model);
        return bpmnXmlConverter.convertToXML(bpmnModel);
    }

    @Override
    public IPage<Model> findByParams(int page, int limit, ModelType modelType, String key, String name) {
        Map<String, Object> params = new HashMap<>(3);
        params.put("type", modelType);
        if (StringUtils.isNotBlank(key)) {
            params.put("modelId", CommonUtil.wrapperWithPercent(key));
        }
        if (StringUtils.isNotBlank(name)) {
            params.put("name", CommonUtil.wrapperWithPercent(name));
        }
        return baseMapper.selectByParams(new Page<>((page - 1L) * limit, limit), params);
    }

    @Override
    public void deploy(Integer id) {
        Model model = getModel(id);
        BpmnModel bpmnModel = getBpmnModel(model);

        repositoryService.createDeployment()
                .name(model.getName())
                .key(model.getModelId())
                .addBpmnModel(model.getModelId() + ".bpmn20.xml", bpmnModel)
                .deploy();
    }
}
