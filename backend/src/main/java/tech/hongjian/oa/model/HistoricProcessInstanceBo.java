package tech.hongjian.oa.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.flowable.engine.history.HistoricProcessInstance;
import tech.hongjian.oa.entity.User;
import tech.hongjian.oa.model.anno.UserInfo;
import tech.hongjian.oa.util.CommonUtil;

import java.util.Date;

/**
 * Created by xiahongjian on 2021/4/21.
 */
@Data
@NoArgsConstructor
public class HistoricProcessInstanceBo {

    private String id;
    private String processInstanceId;
    private String name;
    private String description;

    private String processDefinitionId;
    private String processDefinitionName;
    private String processDefinitionKey;
    private Integer processDefinitionVersion;
    private String deploymentId;
    private String businessKey;

    private Date startTime;
    private Date endTime;
    private Integer startUserId;
    @UserInfo(userField = "startUserId")
    private User startUserInfo;

    public HistoricProcessInstanceBo(HistoricProcessInstance instance) {
        this.id = instance.getId();
        this.processInstanceId = instance.getId();
        this.name = instance.getName();
        this.description = instance.getDescription();
        this.processDefinitionId = instance.getProcessDefinitionId();
        this.processDefinitionName = instance.getProcessDefinitionName();
        this.processDefinitionKey = instance.getProcessDefinitionKey();
        this.processDefinitionVersion = instance.getProcessDefinitionVersion();
        this.deploymentId = instance.getDeploymentId();

        this.businessKey = instance.getBusinessKey();
        this.startTime = instance.getStartTime();
        this.endTime = instance.getEndTime();
        this.startUserId = NumberUtils.isDigits(instance.getStartUserId())
                ? CommonUtil.toInteger(instance.getStartUserId())
                : null;
    }
}
