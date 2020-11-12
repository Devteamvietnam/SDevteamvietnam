package com.devteamvietnam.common.core.redis;


import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

/**
 * spring redis tools
 *
 * @author Ivan
 **/
@SuppressWarnings(value = {"unchecked", "rawtypes" })
@Component
public class RedisCache
{
    @Autowired
    public RedisTemplate redisTemplate;

    /**
     * Cache basic objects, Integer, String, entity classes, etc.
     *
     * @param key cached key value
     * @param value cached value
     */
    public <T> void setCacheObject(final String key, final T value)
    {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * Cache basic objects, Integer, String, entity classes, etc.
     *
     * @param key cached key value
     * @param value cached value
     * @param timeout time
     * @param timeUnit time granularity
     */
    public <T> void setCacheObject(final String key, final T value, final Integer timeout, final TimeUnit timeUnit)
    {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * Set effective time
     *
     * @param key Redis key
     * @param timeout timeout
     * @return true=setting is successful; false=setting failed
     */
    public boolean expire(final String key, final long timeout)
    {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * Set effective time
     *
     * @param key Redis key
     * @param timeout timeout
     * @param unit time unit
     * @return true=setting is successful; false=setting failed
     */
    public boolean expire(final String key, final long timeout, final TimeUnit unit)
    {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * Get the basic object of the cache.
     *
     * @param key cache key value
     * @return cache the data corresponding to the key value
     */
    public <T> T getCacheObject(final String key)
    {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * Delete a single object
     *
     * @param key
     */
    public boolean deleteObject(final String key)
    {
        return redisTemplate.delete(key);
    }

    /**
     * Delete collection objects
     *
     * @param collection multiple objects
     * @return
     */
    public long deleteObject(final Collection collection)
    {
        return redisTemplate.delete(collection);
    }

    /**
     * Cache List data
     *
     * @param key cached key value
     * @param dataList List data to be cached
     * @return cached object
     */
    public <T> long setCacheList(final String key, final List<T> dataList)
    {
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        return count == null? 0: count;
    }

    /**
     * Get the cached list object
     *
     * @param key cached key value
     * @return cache the data corresponding to the key value
     */
    public <T> List<T> getCacheList(final String key)
    {
        return redisTemplate.opsForList().range(key, 0, -1);
    }
    /**
     * Cache Set
     *
     * @param key cache key value
     * @param dataSet cached data
     * @return cache data object
     */
    public <T> long setCacheSet(final String key, final Set<T> dataSet)
    {
        Long count = redisTemplate.opsForSet().add(key, dataSet);
        return count == null? 0: count;
    }

    /**
     * Get the cached set
     *
     * @param key
     * @return
     */
    public <T> Set<T> getCacheSet(final String key)
    {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * Cache Map
     *
     * @param key
     * @param dataMap
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap)
    {
        if (dataMap != null) {
            redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }

    /**
     * Get the cached Map
     *
     * @param key
     * @return
     */
    public <T> Map<String, T> getCacheMap(final String key)
    {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * Save data to Hash
     *
     * @param key Redis key
     * @param hKey Hash key
     * @param value
     */
    public <T> void setCacheMapValue(final String key, final String hKey, final T value)
    {
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    /**
     * Get the data in Hash
     *
     * @param key Redis key
     * @param hKey Hash key
     * @return objects in Hash
     */
    public <T> T getCacheMapValue(final String key, final String hKey)
    {
        HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(key, hKey);
    }

    /**
     * Get data in multiple Hash
     *
     * @param key Redis key
     * @param hKeys Hash key collection
     * @return Hash object collection
     */
    public <T> List<T> getMultiCacheMapValue(final String key, final Collection<Object> hKeys)
    {
        return redisTemplate.opsForHash().multiGet(key, hKeys);
    }

    /**
     * Get a list of cached basic objects
     *
     * @param pattern string prefix
     * @return object list
     */
    public Collection<String> keys(final String pattern)
    {
        return redisTemplate.keys(pattern);
    }
}
