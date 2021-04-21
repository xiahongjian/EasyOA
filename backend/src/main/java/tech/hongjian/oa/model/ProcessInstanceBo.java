package tech.hongjian.oa.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.flowable.engine.runtime.ProcessInstance;
import tech.hongjian.oa.entity.User;
import tech.hongjian.oa.model.anno.UserInfo;
import tech.hongjian.oa.util.CommonUtil;

import java.util.Date;

/**
 * Created by xiahongjian on 2021/4/21.
 */
@Data
@NoArgsConstructor
public class ProcessInstanceBo {

    private String id;
    private boolean suspended;
    private boolean enabled;
    private String activityId;
    private String processInstanceId;
    private String parentId;
    private String superExecutionId;
    private String rootProcessInstanceId;
    private String name;
    private String description;
    private String referenceId;
    private String referenceType;

    private String processDefinitionId;
    private String processDefinitionName;
    private String processDefinitionKey;
    private Integer processDefinitionVersion;
    private String deploymentId;
    private String businessKey;

    private Date startTime;
    private Integer startUserId;
    @UserInfo(userField = "startUserId")
    private User startUserIdUserInfo;
    private String callbackId;
    private String callbackType;

    public ProcessInstanceBo(ProcessInstance instance) {
        this.id = instance.getId();
        this.suspended = instance.isSuspended();
        this.enabled = instance.isEnded();
        this.activityId = instance.getActivityId();
        this.processInstanceId = instance.getProcessInstanceId();
        this.parentId = instance.getParentId();
        this.superExecutionId = instance.getSuperExecutionId();
        this.rootProcessInstanceId = instance.getRootProcessInstanceId();
        this.name = instance.getName();
        this.description = instance.getDescription();
        this.referenceId = instance.getReferenceId();
        this.referenceType = instance.getReferenceType();
        this.processDefinitionId = instance.getProcessDefinitionId();
        this.processDefinitionName = instance.getProcessDefinitionName();
        this.processDefinitionKey = instance.getProcessDefinitionKey();
        this.processDefinitionVersion = instance.getProcessDefinitionVersion();
        this.deploymentId = instance.getDeploymentId();
        this.businessKey = instance.getBusinessKey();
        this.startTime = instance.getStartTime();
        this.startUserId = CommonUtil.toInteger(instance.getStartUserId());
        this.callbackId = instance.getCallbackId();
        this.callbackType = instance.getCallbackType();
    }
}
