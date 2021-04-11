package tech.hongjian.oa.service.impl;

import lombok.Setter;
import org.flowable.engine.HistoryService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hongjian.oa.model.HistoricActivityBo;
import tech.hongjian.oa.model.HistoricTaskBo;
import tech.hongjian.oa.service.BizHistoryService;
import tech.hongjian.oa.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xiahongjian
 * @time 2021/4/11 22:20
 */
@Service
public class BizHistoryServiceImpl implements BizHistoryService {
    @Setter(onMethod_ = {@Autowired})
    private HistoryService historyService;

    @Setter(onMethod_ = {@Autowired})
    private UserService userService;

    @Override
    public List<HistoricActivityBo> listHistoricActivity(String processInstanceId,
                                                         boolean withUserInfo) {
        List<HistoricActivityInstance> list =
                historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceStartTime()
                .list();
        return list.stream().map(e -> createHistoricActivityBo(e, withUserInfo)).collect(Collectors.toList());
    }

    @Override
    public List<HistoricTaskBo> listHistoricTask(String processInstanceId,
                                                 boolean withUserInfo) {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricTaskInstanceStartTime()
                .list();

        return null;
    }

    private HistoricActivityBo createHistoricActivityBo(HistoricActivityInstance instance, boolean withUserInfo) {
        HistoricActivityBo historicActivityBo = new HistoricActivityBo(instance);
        return withUserInfo ? getUserInfo(historicActivityBo) : historicActivityBo;
    }

    private HistoricActivityBo getUserInfo(HistoricActivityBo bo) {
        bo.setAssigneeUserInfo(userService.getUserBriefInfo(bo.getAssignee()));
        return bo;
    }
}
