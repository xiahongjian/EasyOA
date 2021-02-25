package tech.hongjian.oa.controller;


import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.hongjian.oa.entity.User;
import tech.hongjian.oa.entity.enums.Status;
import tech.hongjian.oa.model.R;
import tech.hongjian.oa.model.UserParam;
import tech.hongjian.oa.service.UserService;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xiahongjian
 * @since 2021-01-12
 */
@Setter(onMethod_ = {@Autowired})
@RestController
public class UserController {
    private UserService userService;

    @GetMapping("/user/userinfo")
    public R userInfo() {
        return R.ok(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    @GetMapping("/users")
    public R listUsers(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) Integer status,
                       @RequestParam(required = false) Integer dept,
                       @RequestParam(required = false) String prop,
                       @RequestParam(required = false) String order,
                       @RequestParam Integer page,
                       @RequestParam Integer limit) {
        return R.ok(userService.listUser(keyword, Status.of(status), dept, prop, order, page, limit));
    }

    @PostMapping("/user")
    public R createUser(@RequestBody UserParam formData) {
        userService.createUser(formData);
        return R.ok();
    }

    @PutMapping("/user/{id}")
    public R updateUser(@PathVariable Integer id, @RequestBody User user) {
        userService.updateUser(id, user);
        return R.ok();
    }

    @PutMapping("/user/{id}/reset")
    public R restPassword(@PathVariable Integer id) {
        userService.resetPassword(id);
        return R.ok();
    }

    @GetMapping("/user/{id}")
    public R getUser(@PathVariable Integer id) {
        return R.ok(userService.getById(id));
    }

    @DeleteMapping("/user/{id}")
    public R deleteUser(@PathVariable Integer id) {
        userService.deleteById(id);
        return R.ok();
    }
}
