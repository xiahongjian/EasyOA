package tech.hongjian.oa.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Setter;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntityImpl;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.hongjian.oa.exception.CommonServiceException;
import tech.hongjian.oa.mapper.flowable.ProcessDefinitionMapper;
import tech.hongjian.oa.model.ProcDefVo;
import tech.hongjian.oa.service.ProcessDefinitionService;
import tech.hongjian.oa.util.CommonUtil;

import java.util.stream.Collectors;

/**
 * Created by xiahongjian on 2021/3/31.
 */
@Setter(onMethod_ = {@Autowired})
@Transactional(rollbackFor = Exception.class)
@Service
public class ProcessDefinitionServiceImpl implements ProcessDefinitionService {
    private RepositoryService repoService;
    private ProcessDefinitionMapper processDefinitionMapper;

    @Override
    public IPage<ProcDefVo> listProcessDefinition(String key, String name, Integer suspend, int page, int limit) {


        IPage<ProcessDefinitionEntityImpl> list = processDefinitionMapper.queryByParams(new Page<>(page, limit),
                CommonUtil.wrapWithPercent(key),
                CommonUtil.wrapWithPercent(name),
                suspend);

        Page<ProcDefVo> result = new Page<>();
        result.setRecords(list.getRecords().stream().map(ProcDefVo::new).collect(Collectors.toList()));
        result.setTotal(list.getTotal());
        return result;
    }

    @Override
    public ProcessDefinition getProcDefById(String procDefId) {
        return repoService.createProcessDefinitionQuery().processDefinitionId(procDefId).singleResult();
    }

    @Override
    public void suspend(String procDefId) {
        checkExisted(procDefId);
        repoService.suspendProcessDefinitionById(procDefId, true, null);
    }

    @Override
    public void active(String procDefId) {
        checkExisted(procDefId);
        repoService.activateProcessDefinitionById(procDefId, true, null);
    }

    @Override
    public void delete(String procDefId) {
        ProcessDefinition definition = checkExisted(procDefId);
        repoService.deleteDeployment(definition.getDeploymentId());
    }

    private ProcessDefinition checkExisted(String procDefId) {
        ProcessDefinition procDef = getProcDefById(procDefId);
        if (procDef == null) {
            throw new CommonServiceException("ID为" + procDefId + "的流程定义不存在。");
        }
        return procDef;
    }
}
