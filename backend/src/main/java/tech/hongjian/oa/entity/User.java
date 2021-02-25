package tech.hongjian.oa.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tech.hongjian.oa.entity.enums.Status;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author xiahongjian
 * @since 2021-01-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity implements UserDetails {

    private static final long serialVersionUID = 1L;

    private String username;

    private String password;

    private String name;

    private String email;

    private Integer departmentId;

    @TableField(exist = false)
    private String department;

    private String mobile;

    private String gender;

    private Status status;

    private String avatar;

    private String post;

    @TableField(exist = false)
    private List<Role> authorities;

    @TableField(exist = false)
    private List<String> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return Status.NORMAL == status;
    }


}
