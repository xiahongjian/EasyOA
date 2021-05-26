package tech.hongjian.oa.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.security.core.userdetails.UserDetailsService;
import tech.hongjian.oa.entity.User;
import tech.hongjian.oa.entity.enums.Status;
import tech.hongjian.oa.model.UserVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xiahongjian
 * @since 2021-01-12
 */
public interface UserService extends IService<User>, UserDetailsService {

    IPage<User> listUser(String keyword, Status status, Integer dept, String prop,
                         String order, Integer page, Integer limit);

    User getUserById(Integer id);

    User getUserBriefInfo(Integer id);

    void deleteById(Integer id);

    void batchDelete(Integer[] ids);

    User createUser(User user);

    User createUser(UserVo formData);

    void updateUser(Integer id, User user);

    void updateUser(Integer id, UserVo vo);

    void resetPassword(Integer id);

    boolean usernameIsExisted(String username, Integer excludeId);

    boolean emailIsExisted(String email, Integer excludeId);

    UserVo getUserInfo(Integer id);

    long countByParamMap(Map<String, Object> params);

    List<User> findByParamMap(Map<String, Object> params);

    IPage<User> findByParamMapPage(IPage<User> page, Map<String, Object> params);

    List<User> getUserByIds(Integer[] ids);

    User getUserLeader(Integer userId);
}
