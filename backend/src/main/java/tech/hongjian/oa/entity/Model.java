package tech.hongjian.oa.entity;

import java.sql.Blob;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import tech.hongjian.oa.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.hongjian.oa.entity.enums.ModelStatue;
import tech.hongjian.oa.entity.enums.ModelType;

/**
 * <p>
 * 
 * </p>
 *
 * @author xiahongjian
 * @since 2021-03-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Model extends BaseEntityWithOperator {

    private static final long serialVersionUID = 1L;

    private String name;

    private String modelId;

    private String description;

    private String modelComment;

    private ModelType modelType;

    private String modelEditorJson;

    private byte[] thumbnail;

    @Version
    private Integer version;

    private ModelStatue status;

    // DB中不不在的字段
    @TableField(exist = false)
    private User createdByUser;
    @TableField(exist = false)
    private User updatedByUser;
}
