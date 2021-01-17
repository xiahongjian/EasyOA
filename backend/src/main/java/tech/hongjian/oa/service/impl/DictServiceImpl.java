package tech.hongjian.oa.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import tech.hongjian.oa.entity.Dict;
import tech.hongjian.oa.entity.enums.Status;
import tech.hongjian.oa.mapper.DictMapper;
import tech.hongjian.oa.service.DictService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiahongjian
 * @since 2021-01-12
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Autowired
    private DictMapper dictMapper;

    @Override
    public IPage<Dict> getDict(String name, String key, Status status, int start, int limit) {
        LambdaQueryWrapper<Dict> query = Wrappers.lambdaQuery(Dict.class).orderByAsc(Dict::getCreateTime);
        if (StringUtils.isNotBlank(name)) {
            query.like(Dict::getName, name);
        }
        if (StringUtils.isNotBlank(key)) {
            query.like(Dict::getKey, key);
        }
        if (status != null) {
            query.eq(Dict::getStatus, status);
        }
        return dictMapper.selectPage(new Page<>(start, limit), query);
    }

}
