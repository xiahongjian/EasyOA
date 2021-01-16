package tech.hongjian.oa.entity;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.hongjian.oa.entity.enums.Status;

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
public class Role extends BaseEntity implements GrantedAuthority {

    private static final long serialVersionUID = 1L;

    private String name;

    private String description;

    private String key;

    private Integer sort;

    private Status status;

    @Override
    public String getAuthority() {
        return key;
    }


}
