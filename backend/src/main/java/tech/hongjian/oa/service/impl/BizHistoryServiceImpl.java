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
import tech.hongjian.oa.util.CommonUtil;

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
        return list.stream().map(e -> createHistoricTaskBo(e, withUserInfo)).collect(Collectors.toList());
    }


    private HistoricActivityBo createHistoricActivityBo(HistoricActivityInstance instance, boolean withUserInfo) {
        HistoricActivityBo historicActivityBo = new HistoricActivityBo(instance);
        return withUserInfo ? CommonUtil.fetchUserInfo(historicActivityBo) : historicActivityBo;
    }

    private HistoricTaskBo createHistoricTaskBo(HistoricTaskInstance instance, boolean withUserInfo) {
        HistoricTaskBo historicTaskBo = new HistoricTaskBo(instance);
        return withUserInfo ? CommonUtil.fetchUserInfo(historicTaskBo) : historicTaskBo;
    }
}
