package tech.hongjian.oa.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import tech.hongjian.oa.TestCaseBase;
import tech.hongjian.oa.entity.Role;

/**
 * @author xiahongjian
 * @since  2021-01-16 23:51:56
 */
public class RoleServiceTest extends TestCaseBase {

    @Autowired
    private RoleService roleService;

    @Test
    @Transactional
    public void testFindUserRoles() {
        List<Role> roles = roleService.getUserRoles(1);
        System.out.println(roles);
    }
}
