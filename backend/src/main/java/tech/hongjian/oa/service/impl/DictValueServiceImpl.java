package tech.hongjian.oa.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hongjian.oa.entity.DictValue;
import tech.hongjian.oa.entity.enums.Status;
import tech.hongjian.oa.mapper.DictValueMapper;
import tech.hongjian.oa.service.DictValueService;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xiahongjian
 * @since 2021-01-12
 */
@Setter(onMethod_ = {@Autowired})
@Service()
public class DictValueServiceImpl extends ServiceImpl<DictValueMapper, DictValue> implements DictValueService {

    @Override
    public void deleteAllValues(Integer dictId) {
        if (dictId == null) {
            return;
        }
        getBaseMapper().delete(Wrappers.lambdaQuery(DictValue.class).eq(DictValue::getDictId, dictId));
    }

    @Override
    public List<DictValue> getValueByDictKey(String dictKey) {
        if (StringUtils.isBlank(dictKey)) {
            return Collections.emptyList();
        }
        return getBaseMapper().findValueByDictKey(dictKey, null);
    }

    @Override
    public List<DictValue> getEnableValueByDictKey(String dictKey) {
        if (StringUtils.isBlank(dictKey)) {
            return Collections.emptyList();
        }
        return getBaseMapper().findValueByDictKey(dictKey, Status.NORMAL);
    }
}
