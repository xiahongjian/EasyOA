package tech.hongjian.oa.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hongjian.oa.entity.DictValue;
import tech.hongjian.oa.mapper.DictValueMapper;
import tech.hongjian.oa.service.DictValueService;

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
    private DictValueMapper dictValueMapper;

    @Override
    public void deleteAllValues(Integer dictId) {
        if (dictId == null) {
            return;
        }
        dictValueMapper.delete(lambdaQuery().eq(DictValue::getDictId, dictId));
    }
}
