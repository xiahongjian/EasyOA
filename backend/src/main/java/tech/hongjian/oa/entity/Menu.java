package tech.hongjian.oa.entity;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.hongjian.oa.entity.enums.MenuType;

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
public class Menu extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String title;

    private String icon;

    private Integer sort;

    /**
     * 菜单类型
     */
    private MenuType type;

    private String component;

    private String routePath;

    private String routeName;

    private Integer parentId;

    /**
     * 菜单是否可见
     */
    private Boolean visible;

    private String permission;

    /**
     * 是否为外部链接
     */
    private Boolean externalLink;

    private String method;

    @TableField(exist = false)
    private List<Menu> children = new ArrayList<>();

    @TableField(exist = false)
    private List<Role> roles = new ArrayList<>();
}
