package tech.hongjian.oa.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.flowable.task.api.TaskInfo;
import tech.hongjian.oa.entity.User;
import tech.hongjian.oa.model.anno.UserInfo;
import tech.hongjian.oa.util.CommonUtil;

import java.util.Date;

/**
 * @author xiahongjian
 * @time 2021/4/11 22:49
 */
@Data
@NoArgsConstructor
public class TaskInfoBo {
    private String id;
    private String name;
    private String description;
    private int priority;
    private Integer owner;
    @UserInfo(userField = "owner")
    private User ownerUserInfo;
    private Integer assignee;
    @UserInfo(userField = "assignee")
    private User assigneeUserInfo;
    private Integer submitter;
    @UserInfo(userField = "submitter")
    private User submitterUserInfo;
    private String processInstanceId;
    private String executionId;
    private String taskDefinitionId;
    private String taskDefinitionKey;

    private String category;
    private String formKey;

    private Date createTime;
    private Date dueDate;
    private Date claimTime;

    private String businessKey;

    public TaskInfoBo(TaskInfo info) {
        if (info != null) {
            this.id = info.getId();
            this.name = info.getName();
            this.description = info.getDescription();
            this.priority = info.getPriority();
            this.owner = CommonUtil.toInteger(info.getOwner());
            this.assignee = CommonUtil.toInteger(info.getAssignee());
            this.processInstanceId = info.getProcessInstanceId();
            this.executionId = info.getExecutionId();
            this.taskDefinitionId = info.getTaskDefinitionId();
            this.taskDefinitionKey = info.getTaskDefinitionKey();
            this.category = info.getCategory();
            this.formKey = info.getFormKey();
            this.createTime = info.getCreateTime();
            this.dueDate = info.getDueDate();
            this.claimTime = info.getClaimTime();
        }
    }
}
