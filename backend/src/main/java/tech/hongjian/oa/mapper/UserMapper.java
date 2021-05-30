package tech.hongjian.oa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import tech.hongjian.oa.entity.User;
import tech.hongjian.oa.entity.enums.Status;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author xiahongjian
 * @since 2021-01-12
 */
public interface UserMapper extends BaseMapper<User> {

    IPage<User> selectByParams(IPage<User> objectPage, String keyword, Status status,
                               Integer dept, String prop, String order);

    long countByParamMap(Map<String, Object> params);

    List<User> selectByParamMap(Map<String, Object> params);

    IPage<User> selectByParamMapPage(IPage<User> page, Map<String, Object> params);

    List<Integer> selectUserWithRole(String role);
}
