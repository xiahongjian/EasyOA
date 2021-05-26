package tech.hongjian.oa.flowable.listener;

import lombok.Setter;
import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.task.service.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.hongjian.oa.entity.User;
import tech.hongjian.oa.flowable.FlowConstants;
import tech.hongjian.oa.service.UserService;

/**
 * Created by xiahongjian on 2021/5/26.
 */
@Setter(onMethod_ = {@Autowired})
@Component("leaderSetter")
public class LeaderAssignmentListener  implements TaskListener {
    private UserService userService;

    @Override
    public void notify(DelegateTask delegateTask) {
        Integer submitter = (Integer) delegateTask.getVariable(FlowConstants.V_FLOW_SUBMITTER);
        User leader = userService.getUserLeader(submitter);
        Integer assignee = leader == null ? submitter : leader.getId();
        delegateTask.setAssignee(String.valueOf(assignee));
    }
}
