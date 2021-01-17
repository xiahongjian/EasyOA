package tech.hongjian.oa.service;

import java.util.Set;

/**
 * @author xiahongjian
 * @since  2021-01-16 14:30:54
 */
public interface CacheService {

    public void set(String key, Object value);

    public void setSet(String key, Object vlaue);

    public void setSet(String key, Object... values);

    public Object get(String key);

    public boolean contains(String key);

    public Object remove(String key);

    public Object removeSet(String key, Object value);

    public Set<Object> removeSet(String key, Object... values);
}
