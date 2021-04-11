package tech.hongjian.oa.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.flowable.task.api.Task;

/**
 * Created by xiahongjian on 2021/4/10.
 */
@NoArgsConstructor
@Data
public class TaskBo extends TaskInfoBo {

    private String processDefinitionId;
    private String processDefinitionKey;
    private String processDefinitionName;
    private String parentTaskId;
    private boolean suspended;

    public TaskBo(Task task) {
        super(task);
        if (task != null) {
            this.parentTaskId = task.getParentTaskId();
            this.suspended = task.isSuspended();
        }
    }
}
