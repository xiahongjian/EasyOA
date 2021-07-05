package tech.hongjian.oa.flowable.service;

import tech.hongjian.oa.entity.FlowEntity;

import java.util.Map;

/**
 * Created by xiahongjian on 2021/3/16.
 */
public interface FlowBizFormService<T extends FlowEntity> {
    T getByBizKey(String bizKey);


    /**
     * 将业务表单送入指定流程中
     *
     * @param entity     业务表单
     * @param procDefKey 流程定义key
     * @param variables  变量
     * @return 流程实例ID
     */
    String startProcess(T entity, String procDefKey, Map<String, Object> variables);

    /**
     * 将业务表单送入指定流程中
     *
     * @param entity     业务表单
     * @param procDefKey 流程定义key
     * @return 流程实例ID
     */
    String startProcess(T entity, String procDefKey);

    /**
     * 将业务表单送入默认流程中（使用{@link #supplyProcessDefinitionKey()}返回的值）
     * 如需要指定特定流程，请使用{@link #startProcess(FlowEntity, String)}
     *
     * @param entity 业务表单
     * @return 流程实例ID
     */
    String startProcess(T entity);


    /**
     * 将业务表单送入默认流程中
     *
     * @param entity     业务表单
     * @param variables  变量
     * @return 流程实例ID
     */
    String startProcess(T entity, Map<String, Object> variables);

    /**
     * 返回默认流程定义的key
     *
     * @return 默认流程定义key
     */
    String supplyProcessDefinitionKey();


    void approve(String taskId);

    void approve(String taskId, String comment);

    void reject(String taskId, String comment);

    void completeTask(String taskId, String comment, Map<String, Object> variables);


}
