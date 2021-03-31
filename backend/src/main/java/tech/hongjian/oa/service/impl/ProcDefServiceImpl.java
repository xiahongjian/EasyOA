package tech.hongjian.oa.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Setter;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hongjian.oa.service.ProcDefService;
import tech.hongjian.oa.util.CommonUtil;

import java.util.List;

/**
 * Created by xiahongjian on 2021/3/31.
 */
@Setter(onMethod_ = {@Autowired})
@Service
public class ProcDefServiceImpl implements ProcDefService {
    private RepositoryService repoService;

    @Override
    public IPage<ProcessDefinition> listProcessDefinition(String key, int start, int limit) {

        List<ProcessDefinition> list;
        long total;
        ProcessDefinitionQuery dataQuery = repoService.createProcessDefinitionQuery();
        ProcessDefinitionQuery countQuery = repoService.createProcessDefinitionQuery();
        if (dataQuery != null) {
            String q = CommonUtil.wrapperWithPercent(key);
            list = dataQuery.processDefinitionKeyLike(q).listPage(start, limit);
            total = countQuery.processDefinitionKeyLike(q).count();
        } else {
            list = dataQuery.listPage(start, limit);
            total = countQuery.count();
        }

        Page<ProcessDefinition> result = new Page<>();
        result.setRecords(list);
        result.setTotal(total);
        return result;
    }
}
