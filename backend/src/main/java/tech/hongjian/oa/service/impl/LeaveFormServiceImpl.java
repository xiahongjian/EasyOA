package tech.hongjian.oa.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import tech.hongjian.oa.entity.LeaveForm;
import tech.hongjian.oa.exception.CommonServiceException;
import tech.hongjian.oa.flowable.FlowConstants;
import tech.hongjian.oa.flowable.service.BaseFlowBizFormService;
import tech.hongjian.oa.mapper.LeaveFormMapper;
import tech.hongjian.oa.service.LeaveFormService;
import tech.hongjian.oa.util.CommonUtil;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xiahongjian
 * @since 2021-03-11
 */
@Service
public class LeaveFormServiceImpl extends BaseFlowBizFormService<LeaveFormMapper, LeaveForm> implements LeaveFormService {

    @Override
    public IPage<LeaveForm> listForms(String type, Integer creatorId, Integer page, Integer limit) {
        return baseMapper.queryByParams(new Page<>((page - 1L) * limit, limit), type, creatorId);
    }

    @Override
    public LeaveForm create(LeaveForm form) {
        form = CommonUtil.setEntityDefault(form);
        save(form);
        Map<String, Object> vars = Maps.newHashMap();
        vars.put(FlowConstants.V_ACTION, FlowConstants.APPROVE);
        startProcess(form, supplyProcessDefinitionKey(), vars);
        return form;
    }

    @Override
    public LeaveForm find(Integer id) {
        if (id == null) {
            return null;
        }
        return baseMapper.queryById(id);
    }

    @Override
    public void update(Integer id, LeaveForm form) {
        if (checkExisted(id) == null) {
            return;
        }
        form.setUpdatedAt(LocalDateTime.now());
        updateById(form);
    }

    @Override
    public void delete(Integer id) {
        if (checkExisted(id) == null) {
            return;
        }
        removeById(id);
    }

    @Override
    public void batchDelete(Integer[] ids) {
        if (ids == null || ids.length == 0) {
            return;
        }
        removeByIds(Arrays.asList(ids));
    }

    private LeaveForm checkExisted(Integer id) {
        if (id == null) {
            return null;
        }
        LeaveForm entity = getById(id);
        if (entity == null) {
            throw new CommonServiceException("ID为" + id + "的请假单不存在");
        }
        return entity;
    }

    @Override
    public LeaveForm getByBizKey(String bizKey) {
        if (bizKey == null) {
            return null;
        }
        return getById(flowService.bizKey2Id(bizKey));
    }

    @Override
    public String supplyProcessDefinitionKey() {
        return "LeaveFormFlow";
    }
}
