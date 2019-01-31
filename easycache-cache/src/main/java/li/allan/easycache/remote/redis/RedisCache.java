package li.allan.easycache.remote.redis;

import li.allan.easycache.remote.RemoteCache;

/**
 * @author lialun
 */
public abstract class RedisCache<K, V> implements RemoteCache<K, V> {
    public static RedisCacheBuilder<Object, Object> builder() {
        return new RedisCacheBuilder<>();
    }
}
