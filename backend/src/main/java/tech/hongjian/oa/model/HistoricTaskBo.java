package tech.hongjian.oa.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.flowable.task.api.history.HistoricTaskInstance;

import java.util.Date;

/**
 * @author xiahongjian
 * @time 2021/4/11 22:18
 */
@Setter
@Getter
@NoArgsConstructor
public class HistoricTaskBo extends TaskInfoBo {
    private String deleteReason;
    private Date endTime;
    private Long durationInMillis;
    private Long workTimeInMillis;

    private String comment;

    public HistoricTaskBo(HistoricTaskInstance instance) {
        super(instance);
        if (instance != null) {
            this.deleteReason = instance.getDeleteReason();
            this.endTime = instance.getEndTime();
            this.durationInMillis = instance.getDurationInMillis();
            this.workTimeInMillis = instance.getWorkTimeInMillis();
        }
    }
}
