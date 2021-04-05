package tech.hongjian.oa.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import tech.hongjian.oa.entity.LeaveForm;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiahongjian
 * @since 2021-03-11
 */
public interface LeaveFormService extends IService<LeaveForm> {

    String processDefinitionKey();

    IPage<LeaveForm> listForms(String type, Integer creatorId, Integer page, Integer limit);

    LeaveForm create(LeaveForm form);

    LeaveForm find(Integer id);

    void update(Integer id, LeaveForm form);

    void delete(Integer id);

    void batchDelete(Integer[] ids);
}
