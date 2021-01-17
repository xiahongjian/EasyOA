package tech.hongjian.oa.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import tech.hongjian.oa.entity.Dict;
import tech.hongjian.oa.entity.enums.Status;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiahongjian
 * @since 2021-01-12
 */
public interface DictService extends IService<Dict> {

    public IPage<Dict> getDict(String name, String key, Status status, int start, int limit);
}
