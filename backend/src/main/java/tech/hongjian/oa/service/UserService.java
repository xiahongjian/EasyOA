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

    /**
     * 查找指定用户的上一级领导（如果此用户为自己部门负责人，则会往上再找一层）
     * @param userId
     * @return
     */
    User getUserLeader(Integer userId);

    /**
     * 查找指定用户的领导，根据传入的level决定往上找几级
     * @param userId 需要查找领导用户的ID
     * @param level 向上查找领导的层级
     * @return
     */
    User getUserLeader(Integer userId, int level);

    List<Integer> findUserWithRole(String role);
}
