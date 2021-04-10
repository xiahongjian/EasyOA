package tech.hongjian.oa.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.flowable.engine.repository.ProcessDefinition;
import tech.hongjian.oa.model.ProcDefVo;

/**
 * Created by xiahongjian on 2021/3/31.
 */
public interface BizProcessDefinitionService {
    IPage<ProcDefVo> listProcessDefinition(String key, String name, Integer suspend, int start, int limit);

    ProcessDefinition getProcDefById(String procDefId);

    void suspend(String procDefId);

    void active(String procDefId);

    void delete(String procDefId);
}
