package tech.hongjian.oa.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.hongjian.oa.entity.enums.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author xiahongjian
 * @since 2021-01-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Department extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String name;

    private Integer leaderId;

    private Integer parentId;

    private Status status;

    private Integer sort;

    @TableField(exist = false)
    private List<Department> children = new ArrayList<>();

}
