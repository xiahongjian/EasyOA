package tech.hongjian.oa.entity.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

import lombok.Getter;

/**
 * @author xiahongjian
 * @since  2021-01-12 23:04:51
 */
public enum MenuType implements IEnum<Integer> {
    DIRECTORY(1, "目录"),
    MENU(2, "菜单"),
    BUTTON(3, "按钮"),
    INTERFACE(4, "接口");

    private Integer value;
    @Getter
    private String description;

    MenuType(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
