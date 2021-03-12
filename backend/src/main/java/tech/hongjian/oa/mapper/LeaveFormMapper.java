package tech.hongjian.oa.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import tech.hongjian.oa.entity.LeaveForm;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiahongjian
 * @since 2021-03-11
 */
public interface LeaveFormMapper extends BaseMapper<LeaveForm> {

    IPage<LeaveForm> queryByParams(Page<LeaveForm> page, String type, Integer creatorId);
}
