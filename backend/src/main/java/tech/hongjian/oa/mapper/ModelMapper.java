package tech.hongjian.oa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import tech.hongjian.oa.entity.Model;

import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiahongjian
 * @since 2021-03-20
 */
public interface ModelMapper extends BaseMapper<Model> {
    IPage<Model> selectByParams(IPage<Model> page, Map<String, Object> params);
}
