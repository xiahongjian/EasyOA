package tech.hongjian.oa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import tech.hongjian.oa.model.RestResponse;

/**
 * @author xiahongjian
 * @since  2020-03-18 21:59:22
 */
@Controller
public class IndexController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @ResponseBody
    @GetMapping("/admin/content")
    public RestResponse<?> adminContent() {
        return RestResponse.ok("admin");
    }
}
