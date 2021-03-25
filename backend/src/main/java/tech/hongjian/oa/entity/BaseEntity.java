package tech.hongjian.oa.entity;
/**
 * @author xiahongjian
 * @since  2020-03-16 20:42:53
 */

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Integer id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
