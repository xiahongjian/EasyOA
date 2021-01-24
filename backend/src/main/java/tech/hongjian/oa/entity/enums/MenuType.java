package tech.hongjian.oa.entity.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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

    @JsonValue
    @Override
    public Integer getValue() {
        return value;
    }

    @JsonCreator
    public static MenuType of(Integer value) {
        if (value == null) {
            return null;
        }
        for (MenuType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        return null;
    }
}
