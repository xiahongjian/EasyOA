package tech.hongjian.oa.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.flowable.engine.repository.ProcessDefinition;

/**
 * Created by xiahongjian on 2021/4/1.
 */
@Data
@NoArgsConstructor
public class ProcDefVo {
    private String id;
    private String key;
    private String name;
    private String description;
    private int version;
    private String category;
    private String deploymentId;
    private boolean isSuspended;
    private String tenantId;

    public ProcDefVo(ProcessDefinition definition) {
        this.id = definition.getId();
        this.key = definition.getKey();
        this.name = definition.getName();
        this.description = definition.getDescription();
        this.version = definition.getVersion();
        this.category = definition.getCategory();
        this.deploymentId = definition.getDeploymentId();
        this.tenantId = definition.getTenantId();
        this.isSuspended = definition.isSuspended();
    }
}
