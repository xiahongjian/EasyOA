package tech.hongjian.oa.controller;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.hongjian.oa.model.R;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiahongjian
 * @since 2021-01-12
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/userinfo")
    public R userInfo() {
        return R.ok(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }
}
