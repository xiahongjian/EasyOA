package tech.hongjian.oa.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.HistoryService;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hongjian.oa.model.HistoricProcessInstanceBo;
import tech.hongjian.oa.service.BizHistoricProcessInstanceService;
import tech.hongjian.oa.util.CommonUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by xiahongjian on 2021/4/21.
 */
@Setter(onMethod_ = {@Autowired})
@Service
public class BizHistoricProcessInstanceServiceImpl implements BizHistoricProcessInstanceService {
    private HistoryService historyService;

    @Override
    public IPage<HistoricProcessInstanceBo> listHistoricProcessInstance(String processName, Integer creator, int page, int limit, boolean withUserInfo) {
        HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();
        if (StringUtils.isNotBlank(processName)) {
            query.processInstanceNameLikeIgnoreCase(CommonUtil.wrapWithPercent(processName));
        }
        if (creator != null) {
            query.startedBy(creator.toString());
        }

        List<HistoricProcessInstanceBo> records = query.listPage((page - 1) * limit, limit).stream().map(i -> {
            HistoricProcessInstanceBo historicProcessInstanceBo = new HistoricProcessInstanceBo(i);
            return withUserInfo ? CommonUtil.fetchUserInfo(historicProcessInstanceBo) : historicProcessInstanceBo;
        }).collect(Collectors.toList());
        long total = query.count();
        Page<HistoricProcessInstanceBo> result = new Page<>();
        result.setRecords(records);
        result.setTotal(total);
        return result;
    }
}
