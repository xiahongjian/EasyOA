package tech.hongjian.oa.model;

import com.fasterxml.jackson.annotation.JsonValue;
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

    public enum State {
        RUNNING(1, "启动"),
        FINISHED(2, "结束"),
        SUSPENDED(3, "挂起");
        int value;
        String desc;

        State(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        @JsonValue
        public int getValue() {
            return this.value;
        }
    }

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
    private Long durationInMillis;
    private Long durationStr;
    @UserInfo(userField = "startUserId")
    private User startUserInfo;
    private State state = State.RUNNING;

    private Integer currentAssignee;
    @UserInfo(userField = "currentAssignee")
    private User currentAssigneeUserInfo;
    private String currentTask;
    private String currentTaskName;

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
        instance.getDurationInMillis();
        this.startUserId = NumberUtils.isDigits(instance.getStartUserId())
                ? CommonUtil.toInteger(instance.getStartUserId())
                : null;
    }

    public Long getDurationInMillis() {
        if (endTime != null) {
            if (durationInMillis == null) {
                durationInMillis = endTime.getTime() - startTime.getTime();
            }
            return durationInMillis;
        }
        return System.currentTimeMillis() - startTime.getTime();
    }

    public String getDurationStr() {
        return CommonUtil.durationToStr(getDurationInMillis() / 1000);
    }
}
