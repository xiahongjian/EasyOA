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

    IPage<Dict> getDict(String name, String key, Status status, Integer start, Integer limit);

    Dict getDictById(Integer id);

    Dict create(Dict dict);

    Dict update(Dict dict);

    Dict delete(Integer id);

    boolean exists(Integer id);

    boolean exists(String key);
}
