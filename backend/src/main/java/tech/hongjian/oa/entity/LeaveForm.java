package tech.hongjian.oa.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import tech.hongjian.oa.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author xiahongjian
 * @since 2021-03-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LeaveForm extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Integer requester;

    @TableField(exist = false)
    private String requesterName;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 请假时长（单位小时）
     */
    private Double duration;

    private String leaveType;

    private String reason;

    /**
     * 表单状态
     */
    private Integer status;


}
