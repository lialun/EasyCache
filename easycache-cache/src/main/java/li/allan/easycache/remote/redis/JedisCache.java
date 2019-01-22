package li.allan.easycache.remote.redis;

import li.allan.easycache.BytesValueWrapper;
import li.allan.easycache.ValueWrapper;
import li.allan.easycache.remote.RemoteCache;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.TimeUnit;

/**
 * @author lialun
 */
public class JedisCache implements RemoteCache {
    private JedisPool jedisPool;

    @Override
    public void put(byte[] key, byte[] value, long expireAfterCreate, TimeUnit timeUnit) {
        new JedisOperatorTemplate() {
            @Override
            byte[] readFromRedis() {
                jedis.setex(key, Math.toIntExact(timeUnit.toSeconds(expireAfterCreate)), new BytesValueWrapper(value, TimeUnit.MILLISECONDS.convert(expireAfterCreate, timeUnit) + System.currentTimeMillis()));
                return null;
            }
        }.getResult();
    }

    @Override
    public byte[] get(byte[] key) {
        return new JedisOperatorTemplate() {
            @Override
            byte[] readFromRedis() {
                return jedis.get(key);
            }
        }.getResult();
    }

    @Override
    public BytesValueWrapper getValueWrapper(byte[] key) {
        return null;
    }

    @Override
    public boolean contains(byte[] key) {
        return false;
    }

    @Override
    public void invalidate(byte[] key) {

    }

    @Override
    public long expireTimestampInMills(byte[] key) {
        return 0;
    }

    @Override
    public long size() {
        return 0;
    }

    private abstract class JedisOperatorTemplate {
        Jedis jedis;

        private JedisOperatorTemplate() {
            jedis = jedisPool.getResource();
        }

        abstract byte[] readFromRedis();

        byte[] getResult() {
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
