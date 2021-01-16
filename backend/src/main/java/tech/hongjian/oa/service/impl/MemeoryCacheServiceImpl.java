package tech.hongjian.oa.service.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import tech.hongjian.oa.service.CacheService;

/**
 * @author xiahongjian
 * @since  2021-01-16 14:32:04
 */
// TODO 暂时使用map缓存
@Service
public class MemeoryCacheServiceImpl implements CacheService {

    private static final Map<String, Object> CACHE = new ConcurrentHashMap<>();

    @Override
    public void set(String key, Object value) {
        CACHE.put(key, value);
    }

    @Override
    public Object get(String key) {
        return CACHE.get(key);
    }

    @Override
    public boolean contains(String key) {
        return key == null ? false : CACHE.containsKey(key);
    }

    @Override
    public Object remove(String key) {
        return CACHE.remove(key);
    }

}
