package tech.hongjian.oa.model;

import lombok.Data;
import tech.hongjian.oa.entity.enums.Status;

import java.util.List;

/**
 * @author xiahongjian
 * @time 2021/2/25 23:54
 */
@Data
public class UserParam {
    private String username;

    private String name;

    private String email;

    private Integer departmentId;

    private String mobile;

    private String gender;

    private Status status;

    private String avatar;

    private String post;

    private List<Integer> roles;
}
