package tech.hongjian.oa.entity.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by xiahongjian on 2021/3/11.
 */
@Getter
@AllArgsConstructor
public enum LeaveType implements IEnum<Integer> {
    SELF_LEAVE(0, "事假"),
    ILLNESS_LEAVE(1, "病假");

    Integer value;
    String description;

    @JsonValue
    @Override
    public Integer getValue() {
        return value;
    }

    public static LeaveType of(Integer value) {
        if (value == null) {
            return null;
        }
        for (LeaveType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        return null;
    }
}
