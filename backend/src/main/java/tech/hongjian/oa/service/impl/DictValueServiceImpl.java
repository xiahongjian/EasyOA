package tech.hongjian.oa.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import tech.hongjian.oa.entity.DictValue;
import tech.hongjian.oa.entity.enums.Status;
import tech.hongjian.oa.exception.CommonServiceException;
import tech.hongjian.oa.mapper.DictValueMapper;
import tech.hongjian.oa.service.DictValueService;
import tech.hongjian.oa.util.CommonUtil;

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
    public IPage<DictValue> listDictValue(@NonNull String dictKey, String label, Status status, Integer page, Integer limit) {
        return getBaseMapper().findDictValue(new Page<>((page - 1) * limit, limit), dictKey, label == null ? null : ("%" + label + "%"), status);
    }

    @Override
    public List<DictValue> getEnableValueByDictKey(String dictKey) {
        if (StringUtils.isBlank(dictKey)) {
            return Collections.emptyList();
        }
        return getBaseMapper().findValueByDictKey(dictKey, Status.NORMAL);
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            return;
        }
        lambdaUpdate().eq(DictValue::getId, id).remove();
    }

    @Override
    public void batchDelete(Integer[] ids) {
        if (ids == null || ids.length == 0) {
            return;
        }
        lambdaUpdate().in(DictValue::getId, ids).remove();
    }

    @Override
    public boolean create(DictValue entity) {
        Integer count = lambdaQuery().eq(DictValue::getDictId, entity.getDictId()).eq(DictValue::getValue, entity.getValue()).count();
        if (count > 0) {
            throw new CommonServiceException("字典数据键值[" + entity.getValue() +"]已经存在");
        }
        entity = CommonUtil.setEntityDefault(entity);
        return save(entity);
    }
}
