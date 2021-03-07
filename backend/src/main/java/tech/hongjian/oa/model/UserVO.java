package tech.hongjian.oa.model;

import lombok.Data;
import tech.hongjian.oa.entity.User;
import tech.hongjian.oa.entity.enums.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiahongjian
 * @time 2021/2/25 23:54
 */
@Data
public class UserVO extends User {
    private Integer id;

    private String username;

    private String name;

    private String email;

    private Integer departmentId;

    private String mobile;

    private String gender;

    private Status status;

    private String avatar;

    private String post;

    private List<Integer> roleIds = new ArrayList<>();

}
