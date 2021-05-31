package tech.hongjian.oa.flowable.listener;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.task.service.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.hongjian.oa.entity.User;
import tech.hongjian.oa.service.UserService;
import tech.hongjian.oa.util.CommonUtil;

/**
 * Created by xiahongjian on 2021/5/26.
 */
@Slf4j
@Component("leaderSetter")
public class LeaderAssignmentListener  implements TaskListener {

    @Setter
    private Expression user;
    @Setter
    private Expression level;

    @Setter(onMethod_ = {@Autowired})
    private UserService userService;



    @Override
    public void notify(DelegateTask delegateTask) {
        String userIdStr = (String) user.getValue(delegateTask);
        Long lv = level == null ? 1L : (Long) level.getValue(delegateTask);

        if (userIdStr == null) {
            return;
        }
        Integer userId = CommonUtil.toInteger(userIdStr);
        User leader = userService.getUserLeader(userId, lv.intValue());
        if (leader != null) {
            delegateTask.setAssignee(String.valueOf(leader.getId()));
        }

    }
}
