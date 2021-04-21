package tech.hongjian.oa.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hongjian.oa.model.ProcessInstanceBo;
import tech.hongjian.oa.service.BizProcessInstanceService;
import tech.hongjian.oa.util.CommonUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by xiahongjian on 2021/4/21.
 */
@Service
public class BizProcessInstanceServiceImpl implements BizProcessInstanceService {
    @Setter(onMethod_ = {@Autowired})
    private RuntimeService runtimeService;

    @Override
    public IPage<ProcessInstanceBo> listProcessInstance(String processName, Integer creator, int page, int limit, boolean withUserInfo) {
        ProcessInstanceQuery query = runtimeService.createProcessInstanceQuery();
        if (StringUtils.isNotBlank(processName)) {
            query.processInstanceNameLike(processName);
        }
        if (creator != null) {
            query.startedBy(creator.toString());
        }
        List<ProcessInstanceBo> records = query.listPage((page - 1) * limit, limit).stream().map(i -> {
            ProcessInstanceBo processInstanceBo = new ProcessInstanceBo(i);
            return withUserInfo ? CommonUtil.fetchUserInfo(processInstanceBo) : processInstanceBo;
        }).collect(Collectors.toList());
        long total = query.count();
        Page<ProcessInstanceBo> result = new Page<>();
        result.setRecords(records);
        result.setTotal(total);
        return result;
    }
}
