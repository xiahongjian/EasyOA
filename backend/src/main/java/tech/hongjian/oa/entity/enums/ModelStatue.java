package tech.hongjian.oa.entity.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by xiahongjian on 2021/3/31.
 */
public enum ModelStatue implements IEnum<Integer> {
    NOT_DEPLOYED(0),
    DEPLOYED(1);


    private Integer value;

    ModelStatue(Integer value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public Integer getValue() {
        return value;
    }
}
