package tech.hongjian.oa.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.flowable.engine.repository.ProcessDefinition;
import tech.hongjian.oa.model.ProcDefVo;

/**
 * Created by xiahongjian on 2021/3/31.
 */
public interface ProcessDefinitionService {
    IPage<ProcDefVo> listProcessDefinition(String key, String name, int start, int limit);

    ProcessDefinition getProcDefById(String procDefId);
}
