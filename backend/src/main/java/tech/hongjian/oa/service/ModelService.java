package tech.hongjian.oa.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.hongjian.oa.entity.Model;
import tech.hongjian.oa.entity.enums.ModelType;

import java.io.InputStream;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiahongjian
 * @since 2021-03-20
 */
public interface ModelService extends IService<Model> {

    boolean modelExisted(String modelId, ModelType modelType);

    Model createModel(Model model, Integer createdBy);

    Model updateModel(Model model, Integer updatedBy);

    void deleteModel(Integer id);

    Model importModel(InputStream inputStream, String comment);

    Model getModel(Integer id);

    byte[] getXmlData(Integer id);

    byte[] getXmlData(Model model);

    IPage<Model> findByParams(int page, int limit, ModelType modelType, String key, String name);
}
