package tech.hongjian.oa.entity.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;

/**
 * @author xiahongjian
 * @since 2021-01-12 00:30:50
 */
@Getter
public enum Status implements IEnum<Integer> {
    BAND(0, "停用"),
    NORMAL(1, "正常");

    private Integer value;
    private String description;

    Status(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    @JsonValue
    @Override
    public Integer getValue() {
        return value;
    }

    public static Status of(Integer value) {
        if (value == null) {
            return null;
        }
        for (Status s : values()) {
            if (s.value == value) {
                return s;
            }
        }
        return null;
    }

}
