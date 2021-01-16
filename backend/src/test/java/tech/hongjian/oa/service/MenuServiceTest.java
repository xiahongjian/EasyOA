package tech.hongjian.oa.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import tech.hongjian.oa.TestCaseBase;
import tech.hongjian.oa.entity.Menu;
import tech.hongjian.oa.util.JSONUtil;

/**
 * @author xiahongjian
 * @since  2021-01-14 00:40:14
 */
public class MenuServiceTest extends TestCaseBase {

    @Autowired
    private MenuService menuService;


    @Test
    @Transactional
    public void testMenuTree() {
        List<Menu> tree = menuService.getMenuTree();
        System.out.println(JSONUtil.toJSON(tree));
    }

    public static void main(String[] args) {

    }

    public static class A {
        protected void test() {
            System.out.println("A");
        }
    }
    public static class B extends A {

    }

    public static class C extends B {
        @Override
        protected void test() {
            System.out.println("C");
        }
    }
}
