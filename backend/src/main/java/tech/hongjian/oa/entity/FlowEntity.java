package tech.hongjian.oa.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by xiahongjian on 2021/3/16.
 */
@Getter
@Setter
public abstract class FlowEntity extends BaseEntity {
    private String processInstanceId;
}
