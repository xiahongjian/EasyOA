package tech.hongjian.oa.service.impl;

import org.springframework.stereotype.Service;
import tech.hongjian.oa.service.CacheService;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiahongjian
 * @since 2021-01-16 14:32:04
 */
// TODO 暂时使用map缓存
@Service
public class MemoryCacheServiceImpl implements CacheService {

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
        return key != null && CACHE.containsKey(key);
    }

    @Override
    public Object remove(String key) {
        return CACHE.remove(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setSet(String key, Object vlaue) {
        Set<Object> set = null;
        if (contains(key)) {
            set = (Set<Object>) get(key);
        } else {
            set = new HashSet<>();
        }
        set.add(vlaue);
        set(key, set);
    }

    @Override
    public void setSet(String key, Object... values) {
        if (values != null && values.length > 0) {
            for (int i = 0; i < values.length; i++) {
                setSet(key, values[i]);
            }
        }
    }

    @Override
    public Object removeSet(String key, Object value) {
        if (!contains(key)) {
            return null;
        }
        @SuppressWarnings("unchecked")
        Set<Object> values = (Set<Object>) get(key);
        if (values.contains(values)) {
            values.remove(value);
            // 没有元素时，删除set
            if (values.isEmpty()) {
                remove(key);
            }
            return value;
        }

        return null;
    }

    @Override
    public Set<Object> removeSet(String key, Object... values) {
        if (values != null && values.length > 0) {
            Set<Object> set = new HashSet<>();
            for (int i = 0; i < values.length; i++) {
                Object res = removeSet(key, values[i]);
                if (res != null) {
                    set.add(res);
                }
            }
            return set;
        }
        return null;
    }

    public Map<String, Object> getAllData() {
        return CACHE;
    }
}
