package tech.hongjian.oa.service;

import tech.hongjian.oa.entity.DictValue;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiahongjian
 * @since 2021-01-12
 */
public interface DictValueService extends IService<DictValue> {

    void deleteAllValues(Integer id);
}
