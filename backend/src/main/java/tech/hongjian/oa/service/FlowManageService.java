package tech.hongjian.oa.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.flowable.ui.modeler.domain.AbstractModel;

/**
 * @author xiahongjian
 * @time 2021/3/19 22:14
 */
public interface FlowManageService {

    IPage<AbstractModel> listModels(String name, String key, int start, int limit);
}
