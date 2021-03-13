package tech.hongjian.oa.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import tech.hongjian.oa.entity.LeaveForm;
import tech.hongjian.oa.exception.CommonServiceException;
import tech.hongjian.oa.mapper.LeaveFormMapper;
import tech.hongjian.oa.service.LeaveFormService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiahongjian
 * @since 2021-03-11
 */
@Service
public class LeaveFormServiceImpl extends ServiceImpl<LeaveFormMapper, LeaveForm> implements LeaveFormService {

    @Override
    public IPage<LeaveForm> listForms(String type, Integer creatorId, Integer page, Integer limit) {
        return baseMapper.queryByParams(new Page<>((page - 1L) * limit, limit), type, creatorId);
    }

    @Override
    public LeaveForm create(LeaveForm form) {
        save(form);
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
        form.setUpdateTime(LocalDateTime.now());
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
}
