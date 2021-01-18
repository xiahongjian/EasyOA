package tech.hongjian.oa.mapper;

import tech.hongjian.oa.entity.DictValue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.hongjian.oa.entity.enums.Status;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author xiahongjian
 * @since 2021-01-12
 */
public interface DictValueMapper extends BaseMapper<DictValue> {

    List<DictValue> findValueByDictKey(String dictKey, Status status);

}
