package tech.hongjian.oa.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.flowable.engine.history.HistoricActivityInstance;
import tech.hongjian.oa.entity.User;
import tech.hongjian.oa.util.CommonUtil;

import java.util.Date;

/**
 * @author xiahongjian
 * @time 2021/4/11 22:19
 */
@Data
@NoArgsConstructor
public class HistoricActivityBo {
    private String id;
    private String activityId;
    private String activityName;
    private String activityType;
    private String processDefinitionId;
    private String processInstanceId;
    private String executionId;
    private String taskId;
    private String calledProcessInstanceId;
    private Integer assignee;
    private User assigneeUserInfo;
    private Date startTime;
    private Date endTime;
    private Long durationInMillis;
    private String deleteReason;

    public HistoricActivityBo(HistoricActivityInstance instance) {
        this.id = instance.getId();
        this.activityId = instance.getActivityId();
        this.activityName = instance.getActivityName();
        this.activityType = instance.getActivityType();
        this.processDefinitionId = instance.getProcessDefinitionId();
        this.processInstanceId = instance.getProcessInstanceId();
        this.executionId = instance.getExecutionId();
        this.taskId = instance.getTaskId();
        this.calledProcessInstanceId = instance.getCalledProcessInstanceId();
        this.assignee = CommonUtil.toInteger(instance.getAssignee());
        this.startTime = instance.getStartTime();
        this.endTime = instance.getEndTime();
        this.durationInMillis = instance.getDurationInMillis();
        this.deleteReason = instance.getDeleteReason();
    }
}
