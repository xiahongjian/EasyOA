package tech.hongjian.oa.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.BpmnAutoLayout;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.Process;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.flowable.ui.common.util.XmlUtil;
import org.flowable.validation.ProcessValidator;
import org.flowable.validation.ProcessValidatorFactory;
import org.flowable.validation.ValidationError;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tech.hongjian.oa.entity.Model;
import tech.hongjian.oa.entity.User;
import tech.hongjian.oa.entity.enums.ModelType;
import tech.hongjian.oa.exception.CommonServiceException;
import tech.hongjian.oa.mapper.ModelMapper;
import tech.hongjian.oa.service.ModelService;
import tech.hongjian.oa.util.CommonUtil;
import tech.hongjian.oa.util.WebUtil;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
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

    @Override
    public Model createModel(Model model, Integer createdBy) {
        Model entity = new Model();
        BeanUtils.copyProperties(model, entity);
        CommonUtil.setEntityDefault(entity, createdBy);
        save(entity);
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
    public Model createOrUpdateProcessModel(InputStream inputStream) {
        XMLInputFactory xif = XmlUtil.createSafeXmlInputFactory();
        try {
            InputStreamReader xmlIn = new InputStreamReader(inputStream, "UTF-8");
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
            User currentUser = WebUtil.currentUser();
            //查询是否已经存在流程模板
            Model newModel = new Model();
            Model existedModel = lambdaQuery().eq(Model::getKey, process.getId()).eq(Model::getModelType, ModelType.BPMN).one();
            boolean isUpdate = false;
            if (existedModel != null) {
                isUpdate = true;
                newModel = existedModel;
            }
            newModel.setName(name);
            newModel.setKey(process.getId());
            newModel.setModelType(ModelType.BPMN);
            newModel.setDescription(description);
            newModel.setModelEditorJson(modelNode.toString());

            if (isUpdate) {
                return updateModel(newModel, currentUser.getId());
            } else {
                return createModel(newModel, currentUser.getId());
            }
        } catch (UnsupportedEncodingException | XMLStreamException e) {
            log.warn("导入流程模板失败，原因：{}", e.getMessage(), e);
            throw new CommonServiceException("导入流程模板失败，原因：" + e.getMessage());
        }
    }

    @Override
    public IPage<Model> findByParams(int page, int limit, ModelType modelType, String key, String name) {
        Map<String, Object> params = new HashMap<>(3);
        params.put("type", modelType);
        if (StringUtils.isNotBlank(key)) {
            params.put("key", CommonUtil.wrapperWithPercent(key));
        }
        if (StringUtils.isNotBlank(name)) {
            params.put("name", CommonUtil.wrapperWithPercent(name));
        }
        return baseMapper.selectByParams(new Page<>((page - 1) * limit, limit), params);
    }

}
