package tech.hongjian.oa.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.baomidou.mybatisplus.extension.service.IService;

import tech.hongjian.oa.entity.User;
import tech.hongjian.oa.entity.enums.Status;
import tech.hongjian.oa.model.UserVO;

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

    void deleteById(Integer id);

    void batchDelete(Integer[] ids);

    User createUser(User user);

    User createUser(UserVO formData);

    void updateUser(Integer id, User user);

    void updateUser(Integer id, UserVO vo);

    void resetPassword(Integer id);

    boolean usernameIsExisted(String username, Integer excludeId);

    boolean emailIsExisted(String email, Integer excludeId);

    UserVO getUserInfo(Integer id);
}
