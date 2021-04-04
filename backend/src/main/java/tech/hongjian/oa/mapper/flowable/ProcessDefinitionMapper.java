package tech.hongjian.oa.mapper.flowable;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntityImpl;

/**
 * Created by xiahongjian on 2021/4/4.
 */
public interface ProcessDefinitionMapper  extends BaseMapper<ProcessDefinitionEntityImpl> {
    IPage<ProcessDefinitionEntityImpl> queryByParams(IPage<ProcessDefinitionEntityImpl> page, String key, String name, Integer suspend);
}
