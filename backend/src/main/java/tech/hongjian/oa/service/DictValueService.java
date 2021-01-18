package tech.hongjian.oa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.hongjian.oa.entity.DictValue;

import java.util.List;

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

    List<DictValue> getValueByDictKey(String dictKey);

    List<DictValue> getEnableValueByDictKey(String dictKey);
}
