package tech.hongjian.oa.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import tech.hongjian.oa.entity.LeaveForm;
import tech.hongjian.oa.mapper.LeaveFormMapper;
import tech.hongjian.oa.service.LeaveFormService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
    public IPage<LeaveForm> listForms(String type, Integer requester, Integer page, Integer limit) {
        return baseMapper.queryByParams(new Page<>((page - 1L) * limit, limit), type, page);
    }
}
