package li.allan.easycache.cache;

import li.allan.easycache.ValueWrapper;
import li.allan.easycache.annotation.CacheType;
import li.allan.easycache.config.EasyCacheConfig;
import li.allan.easycache.remote.serializer.Serializer;

/**
 * @author lialun
 */
public class CacheOperator {
    public static void put(CacheType cacheType, String cacheName, String cacheKey, Object value, Class<? extends Serializer> valueSerializer, long expireInSecond, int cacheSize) {
        AbstractOperator abstractOperator = getOperator(cacheType);
        abstractOperator.put(cacheName, cacheKey, value, valueSerializer, expireInSecond, cacheSize);
    }

    public static <V> ValueWrapper<V> get(CacheType cacheType, String cacheName, String cacheKey, Class<? extends Serializer> valueSerializer, Class<V> type) {
        AbstractOperator abstractOperator = getOperator(cacheType);

        return abstractOperator.get(cacheName, cacheKey, valueSerializer, type);
    }

    public static void remove(CacheType cacheType, String cacheName, String cacheKey) {
        AbstractOperator abstractOperator = getOperator(cacheType);
        abstractOperator.remove(cacheName, cacheKey);
    }

    private static AbstractOperator getOperator(CacheType cacheType) {
        if (cacheType.equals(CacheType.Local)) {
            return EasyCacheConfig.getLocalCacheOperator();
        } else {
            return EasyCacheConfig.getRemoteCacheOperator();
        }
    }
}
