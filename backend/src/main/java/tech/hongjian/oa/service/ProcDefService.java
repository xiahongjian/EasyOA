package tech.hongjian.oa.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.flowable.engine.repository.ProcessDefinition;

/**
 * Created by xiahongjian on 2021/3/31.
 */
public interface ProcDefService {
    IPage<ProcessDefinition> listProcessDefinition(String key, int start, int limit);
}
