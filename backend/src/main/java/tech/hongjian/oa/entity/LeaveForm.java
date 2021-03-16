package tech.hongjian.oa.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

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
public class LeaveForm extends FlowEntity {

    private static final long serialVersionUID = 1L;

    private Integer creatorId;

    @TableField(exist = false)
    private User creator;

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


    /**
     * 详细原因
     */
    private String detailReason;
}
