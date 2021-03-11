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
public enum WorkFlowStatus implements IEnum<Integer> {
    DRAFT(0, "草稿"),
    WORKING(1, "审核中"),
    COMPLETED(2, "完成"),
    ABORTED(3, "作废");

    Integer value;
    String description;

    @JsonValue
    @Override
    public Integer getValue() {
        return value;
    }

    public static WorkFlowStatus of(Integer value) {
        if (value == null) {
            return null;
        }
        for (WorkFlowStatus status : values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        return null;
    }
}
