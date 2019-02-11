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

    JedisCache(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public void put(K key, V value, long expireAfterCreate, TimeUnit timeUnit, Class<? extends Serializer> keySerializer, Class<? extends Serializer> valueSerializer) {
        new JedisOperatorTemplate() {
            @Override
            Object readFromRedis() {
                byte[] keyBytes = SerializerContainer.getSerializer(keySerializer).serialize(key);
                ValueWrapper<V> valueWrapper = new ValueWrapper<>(value, TimeUnit.MILLISECONDS.convert(expireAfterCreate, timeUnit) + System.currentTimeMillis());
                byte[] valueWrapperBytes = SerializerContainer.getSerializer(valueSerializer).serialize(valueWrapper);
                jedis.setex(keyBytes, Math.max(1, (int) timeUnit.toSeconds(expireAfterCreate)), valueWrapperBytes);
                return null;
            }
        }.getResult();
    }

    @Override
    public V get(K key, Class<? extends Serializer> keySerializer, Class<? extends Serializer> valueSerializer) {
        ValueWrapper<V> valueWrapper = getValueWrapper(key, keySerializer, valueSerializer);
        return valueWrapper == null ? null : valueWrapper.getValue();
    }

    @Override
    public ValueWrapper<V> getValueWrapper(K key, Class<? extends Serializer> keySerializer, Class<? extends Serializer> valueSerializer) {
        return new JedisOperatorTemplate<ValueWrapper<V>>() {
            @Override
            ValueWrapper<V> readFromRedis() {
                byte[] keyBytes = SerializerContainer.getSerializer(keySerializer).serialize(key);
                return (ValueWrapper<V>) SerializerContainer.getSerializer(valueSerializer).deserialize(jedis.get(keyBytes), ValueWrapper.class);
            }
        }.getResult();
    }

    @Override
    public boolean contains(K key, Class<? extends Serializer> keySerializer) {
        return new JedisOperatorTemplate<Boolean>() {
            @Override
            Boolean readFromRedis() {
                byte[] keyBytes = SerializerContainer.getSerializer(keySerializer).serialize(key);
                return jedis.exists(keyBytes);
            }
        }.getResult();
    }

    @Override
    public void invalidate(K key, Class<? extends Serializer> keySerializer) {
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
    public long expireTimestampInMills(K key, Class<? extends Serializer> keySerializer) {
        return new JedisOperatorTemplate<Long>() {
            @Override
            Long readFromRedis() {
                byte[] keyBytes = SerializerContainer.getSerializer(keySerializer).serialize(key);
                long ttl = jedis.ttl(keyBytes);
                return ttl > 0 ? (ttl * 1000 + System.currentTimeMillis()) : ttl;
            }
        }.getResult();
    }

    @Override
    public long size() {
        return -1;
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
