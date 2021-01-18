package tech.hongjian.oa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.hongjian.oa.entity.Dict;
import tech.hongjian.oa.entity.enums.Status;
import tech.hongjian.oa.exception.CommonServiceException;
import tech.hongjian.oa.mapper.DictMapper;
import tech.hongjian.oa.service.DictService;
import tech.hongjian.oa.service.DictValueService;

import java.text.MessageFormat;
import java.time.LocalDateTime;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xiahongjian
 * @since 2021-01-12
 */
@Setter(onMethod_ = {@Autowired})
@Transactional(rollbackFor = Exception.class)
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    public interface M {
        String KEY_CANT_NOT_BE_NULL = "字典类型不能为空";
        String ALREADY_EXISTED = "字典类型: [{1}]已经存在";
        String NOT_EXISTED = "字典类型: [{1}]不存在";
        String PARAM_ERROR = "参数错误";
    }

    private DictValueService dictValueService;

    @Override
    public IPage<Dict> getDict(String name, String key, Status status, int page,
                               int limit) {
        LambdaQueryChainWrapper<Dict> query =
                lambdaQuery().orderByAsc(Dict::getCreateTime);
        if (StringUtils.isNotBlank(name)) {
            query.like(Dict::getName, name);
        }
        if (StringUtils.isNotBlank(key)) {
            query.like(Dict::getKey, key);
        }
        if (status != null) {
            query.eq(Dict::getStatus, status);
        }
        return query.page(new Page<>(limit * (page - 1), limit));
    }

    @Override
    public Dict getDictByKey(String dictKey) {
        if (dictKey == null) {
            return null;
        }
        return lambdaQuery().eq(Dict::getKey, dictKey).one();
    }

    @Override
    public Dict create(Dict dict) {
        if (dict == null) {
            return null;
        }
        if (StringUtils.isBlank(dict.getKey())) {
            throw new CommonServiceException(M.KEY_CANT_NOT_BE_NULL);
        }
        if (exists(dict.getKey())) {
            throw new CommonServiceException(MessageFormat.format(M.ALREADY_EXISTED,
                    dict.getKey()));
        }
        return null;
    }

    @Override
    public Dict update(Dict dict) {
        if (dict.getId() == null || getBaseMapper().selectById(dict.getId()) == null) {
            throw new CommonServiceException(M.PARAM_ERROR);
        }
        dict.setUpdateTime(LocalDateTime.now());
        getBaseMapper().updateById(dict);
        return dict;
    }

    @Override
    public Dict delete(String dictKey) {
        Dict dict = getDictByKey(dictKey);
        if (dict == null) {
            throw new CommonServiceException(M.NOT_EXISTED);
        }
        getBaseMapper().deleteById(dict.getId());
        // 删除关联的值
        dictValueService.deleteAllValues(dict.getId());
        return dict;
    }

    @Override
    public boolean exists(String dictKey) {
        if (dictKey == null) {
            return false;
        }
        return getDictByKey(dictKey) != null;
    }

}
