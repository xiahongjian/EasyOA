package tech.hongjian.oa.service;

import tech.hongjian.oa.entity.FlowEntity;

import java.util.Map;

/**
 * Created by xiahongjian on 2021/3/16.
 */
public interface FlowService {
    String id2BizKey(String clazzName, Integer id);

    Integer bizKey2Id(String bizKey);

    String getBizFormClassName(String bizKey);

    <T extends FlowEntity> String startProcess(T form, String processDefKey, Map<String, Object> variables);

    <T extends FlowEntity> String startProcess(T form, String processDefKey);

    <T extends FlowEntity> String startProcess(Class<T> clazz, Integer id, String processDefKey, Map<String, Object> variables);

    <T extends FlowEntity> String startProcess(Class<T> clazz, Integer id, String processDefKey);

    <T extends FlowEntity> T getBizFormByBizKey(String bizKey);

    <T extends FlowEntity> void updateBizFormVariable(String processInstanceId, T bizForm);

    void approve(String taskId);

    void reject(String taskId, String comment);
}
