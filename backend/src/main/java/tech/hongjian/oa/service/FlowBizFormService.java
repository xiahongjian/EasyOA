package tech.hongjian.oa.service;

import tech.hongjian.oa.entity.FlowEntity;

/**
 * Created by xiahongjian on 2021/3/16.
 */
public interface FlowBizFormService<T extends FlowEntity> {
    T getByBizKey(String bizKey);
}
