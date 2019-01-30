package li.allan.easycache.remote.redis;

import li.allan.easycache.ValueWrapper;
import li.allan.easycache.remote.serializer.Serializer;
import li.allan.easycache.remote.serializer.SerializerContainer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.TimeUnit;

/**
 * @author lialun
 */
public class JedisCache<K, V> extends RedisCache<K, V> {
    private JedisPool jedisPool;

    public static RedisCacheBuilder<Object, Object> builder() {
        return new RedisCacheBuilder<>();
    }

    JedisCache(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public void put(K key, V value, long expireAfterCreate, TimeUnit timeUnit, Class<Serializer> keySerializer, Class<Serializer> valueSerializer) {
        new JedisOperatorTemplate() {
            @Override
            Object readFromRedis() {
                byte[] keyBytes = SerializerContainer.getSerializer(keySerializer).serialize(key);
                ValueWrapper<V> valueWrapper = new ValueWrapper<>(value, TimeUnit.MILLISECONDS.convert(expireAfterCreate, timeUnit) + System.currentTimeMillis());
                byte[] valueWrapperBytes = SerializerContainer.getSerializer(keySerializer).serialize(valueWrapper);
                jedis.setex(keyBytes, Math.toIntExact(timeUnit.toSeconds(expireAfterCreate)), valueWrapperBytes);
                return null;
            }
        }.getResult();
    }

    @Override
    public V get(K key, Class<Serializer> keySerializer) {
        ValueWrapper<V> valueWrapper = getValueWrapper(key, keySerializer);
        return valueWrapper == null ? null : valueWrapper.getValue();
    }

    @Override
    public ValueWrapper<V> getValueWrapper(K key, Class<Serializer> keySerializer) {
        return new JedisOperatorTemplate<ValueWrapper<V>>() {
            @Override
            ValueWrapper<V> readFromRedis() {
                byte[] keyBytes = SerializerContainer.getSerializer(keySerializer).serialize(key);
                return (ValueWrapper<V>) SerializerContainer.getSerializer(keySerializer).deserialize(jedis.get(keyBytes), ValueWrapper.class);
            }
        }.getResult();
    }

    @Override
    public boolean contains(K key, Class<Serializer> keySerializer) {
        return new JedisOperatorTemplate<Boolean>() {
            @Override
            Boolean readFromRedis() {
                byte[] keyBytes = SerializerContainer.getSerializer(keySerializer).serialize(key);
                return jedis.exists(keyBytes);
            }
        }.getResult();
    }

    @Override
    public void invalidate(K key, Class<Serializer> keySerializer) {
        new JedisOperatorTemplate() {
            @Override
            Boolean readFromRedis() {
                byte[] keyBytes = SerializerContainer.getSerializer(keySerializer).serialize(key);
                jedis.del(keyBytes);
                return null;
            }
        }.getResult();
    }

    @Override
    public long expireTimestampInMills(K key, Class<Serializer> keySerializer) {
        return new JedisOperatorTemplate<Long>() {
            @Override
            Long readFromRedis() {
                byte[] keyBytes = SerializerContainer.getSerializer(keySerializer).serialize(key);
                long ttl = jedis.ttl(keyBytes);
                return ttl > 0 ? ttl * 1000 : ttl;
            }
        }.getResult();
    }

    @Override
    public long size() {
        return 0;
    }

    private abstract class JedisOperatorTemplate<T> {
        Jedis jedis;

        private JedisOperatorTemplate() {
            jedis = jedisPool.getResource();
        }

        abstract T readFromRedis();

        T getResult() {
            try {
                return readFromRedis();
            } finally {
                if (jedis != null) {
                    try {
                        jedis.close();
                    } catch (Exception e) {
                        //ignore
                    }
                }
            }
        }
    }
}
