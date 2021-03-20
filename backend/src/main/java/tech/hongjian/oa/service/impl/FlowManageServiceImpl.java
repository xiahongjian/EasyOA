package tech.hongjian.oa.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ModelQuery;
import org.flowable.ui.modeler.domain.AbstractModel;
import org.flowable.ui.modeler.serviceapi.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hongjian.oa.service.FlowManageService;

import java.util.List;

/**
 * @author xiahongjian
 * @time 2021/3/19 22:14
 */
@Setter(onMethod_ = {@Autowired})
@Service
public class FlowManageServiceImpl implements FlowManageService {
    private ModelService modelService;
    private RepositoryService repoService;

    @Override
    public IPage<AbstractModel> listModels(String name, String key, int start, int limit) {
        modelService
        modelService.getModelsByModelType(AbstractModel.MODEL_TYPE_BPMN);

    }
}
