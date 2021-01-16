package tech.hongjian.oa.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import tech.hongjian.oa.TestCaseBase;
import tech.hongjian.oa.entity.User;

/**
 * @author xiahongjian
 * @since  2020-03-18 22:00:36
 */
public class UserServiceTest extends TestCaseBase {
    @Autowired
    private UserService userService;

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
    @Transactional
    public void testGetUserRole() {
    }

}
