package tech.hongjian.oa.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.flowable.task.api.Task;

import java.util.Date;

/**
 * Created by xiahongjian on 2021/4/10.
 */
@NoArgsConstructor
@Data
public class TaskVo {
    private String id;
    private String name;
    private String description;
    private int priority;
    private Integer owner;
    private Integer assignee;
    private String processInstanceId;
    private String executionId;
    private String taskDefinitionId;
    private String taskDefinitionKey;
    private String category;
    private String parentTaskId;
    private String formKey;
    private boolean suspended;
    private Date createTime;
    private Date dueDate;
    private Date claimTime;

    public TaskVo(Task task) {
        if (task != null) {
            this.id = task.getId();
            this.name = task.getName();
            this.description = task.getDescription();
            this.priority = task.getPriority();
            this.owner = Integer.valueOf(task.getOwner());
            this.assignee = Integer.valueOf(task.getAssignee());
            this.processInstanceId = task.getProcessInstanceId();
            this.executionId = task.getExecutionId();
            this.taskDefinitionId = task.getTaskDefinitionId();
            this.taskDefinitionKey = task.getTaskDefinitionKey();
            this.category = task.getCategory();
            this.parentTaskId = task.getParentTaskId();
            this.formKey = task.getFormKey();
            this.suspended = task.isSuspended();
            this.createTime = task.getCreateTime();
            this.dueDate = task.getDueDate();
            this.claimTime = task.getClaimTime();
        }
    }
}
