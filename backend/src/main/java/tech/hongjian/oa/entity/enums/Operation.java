package tech.hongjian.oa.entity.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/** 
 * @author xiahongjian
 * @since  2020-03-17 00:13:35
 */
public enum Operation implements IEnum<Integer> {
    GET(1, "查询"), 
    POST(2, "创建"), 
    PUT(3, "更新"), 
    DELETE(4, "删除"),
    ALL(5, "全部");
    
    @JsonValue
    int value;
    String description;
    
    Operation(int value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
