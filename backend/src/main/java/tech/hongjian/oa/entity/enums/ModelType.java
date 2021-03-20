package tech.hongjian.oa.entity.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * Created by xiahongjian on 2021/3/20.
 */
public enum ModelType implements IEnum<Integer> {
    BPMN(0),
    FORM(2),
    APP(3),
    DECISION_TABLE(4),
    CMMN(5),
    DECISION_SERVICE(6);

    private Integer value;

    ModelType(Integer value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return null;
    }
}
