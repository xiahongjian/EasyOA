package tech.hongjian.oa.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import tech.hongjian.oa.TestCaseBase;
import tech.hongjian.oa.entity.Permission;
import tech.hongjian.oa.entity.User;
import tech.hongjian.oa.entity.enums.Operation;

/** 
 * @author xiahongjian
 * @since  2020-03-18 22:00:36
 */
public class UserServiceTest extends TestCaseBase {
    @Autowired
    private UserService userService;
    
    @Autowired
    private PermissionService permissionService;
    
    @Test
    @Transactional
    public void testInsert() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword(new BCryptPasswordEncoder().encode("1"));
        user.setName("系统管理员");
        user.setEmail("admin@hongjian.tech");
        userService.save(user);
    }
    
    
    @Test
    public void testInsertPermission() {
        Permission perm = new Permission();
        perm.setName("user");
        perm.setDescription("user");
        perm.setUrl("/user/**");
        perm.setOperation(Operation.ALL);
        permissionService.save(perm);
    }
}
