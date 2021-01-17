package tech.hongjian.oa.entity.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;

/**
 * @author xiahongjian
 * @since  2021-01-12 00:09:04
 */
@Getter
public enum MenuStatus implements IEnum<Integer> {
    HIDE(0, "隐藏"),
    SHOW(1, "显示");

    private Integer value;
    private String description;

    MenuStatus(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    @JsonValue
    @Override
    public Integer getValue() {
        return value;
    }

}
