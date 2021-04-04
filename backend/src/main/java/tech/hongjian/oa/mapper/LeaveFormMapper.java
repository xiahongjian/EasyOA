package tech.hongjian.oa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import tech.hongjian.oa.entity.LeaveForm;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author xiahongjian
 * @since 2021-03-11
 */
public interface LeaveFormMapper extends BaseMapper<LeaveForm> {

    IPage<LeaveForm> queryByParams(IPage<LeaveForm> page, String type, Integer creatorId);

    LeaveForm queryById(Integer id);
}
