package tech.hongjian.oa.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hongjian.oa.model.ProcDefVo;
import tech.hongjian.oa.service.ProcessDefinitionService;
import tech.hongjian.oa.util.CommonUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by xiahongjian on 2021/3/31.
 */
@Setter(onMethod_ = {@Autowired})
@Service
public class ProcessDefinitionServiceImpl implements ProcessDefinitionService {
    private RepositoryService repoService;

    @Override
    public IPage<ProcDefVo> listProcessDefinition(String key, String name, int page, int limit) {

        List<ProcessDefinition> list;
        long total;
        ProcessDefinitionQuery dataQuery = repoService.createProcessDefinitionQuery();
        ProcessDefinitionQuery countQuery = repoService.createProcessDefinitionQuery();

        if (StringUtils.isNotBlank(key)) {
            String q = CommonUtil.wrapperWithPercent(key);
            dataQuery.processDefinitionKeyLike(q);
            countQuery.processDefinitionKeyLike(q);
        }
        if (StringUtils.isNotBlank(name)) {
            String q = CommonUtil.wrapperWithPercent(name);
            dataQuery.processDefinitionKeyLike(q);
            countQuery.processDefinitionKeyLike(q);
        }
        list = dataQuery.listPage((page - 1) * limit, limit);
        total = countQuery.count();

        Page<ProcDefVo> result = new Page<>();
        result.setRecords(list.stream().map(ProcDefVo::new).collect(Collectors.toList()));
        result.setTotal(total);
        return result;
    }

    @Override
    public ProcessDefinition getProcDefById(String procDefId) {
        return repoService.createProcessDefinitionQuery().processDefinitionId(procDefId).singleResult();
    }
}
