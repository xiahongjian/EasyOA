package tech.hongjian.oa.entity;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import tech.hongjian.oa.entity.enums.Operation;

/**
 * <p>
 * 
 * </p>
 *
 * @author xiahongjian
 * @since 2020-03-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Permission extends BaseEntity implements GrantedAuthority {

    private static final long serialVersionUID = 1L;

    private String name;

    private String description;

    private String url;

    private Operation operation;

    private String group;

    private String groupDesc;

    @Override
    public String getAuthority() {
        return url + ";" + operation;
    }


}
