package tech.hongjian.oa.service;
/**
 * @author xiahongjian
 * @since  2021-01-16 14:30:54
 */
public interface CacheService {

    public void set(String key, Object value);

    public Object get(String key);

    public boolean contains(String key);

    public Object remove(String key);
}
