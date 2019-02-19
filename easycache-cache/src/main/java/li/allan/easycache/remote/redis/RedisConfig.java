package li.allan.easycache.remote.redis;

import li.allan.easycache.RemoteCacheConfig;
import redis.clients.jedis.JedisPool;

import java.util.Objects;

/**
 * @author lialun
 */
public class RedisConfig implements RemoteCacheConfig {
    private JedisPool jedisPool;

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RedisConfig that = (RedisConfig) o;
        return Objects.equals(jedisPool, that.jedisPool);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jedisPool);
    }

    @Override
    public String toString() {
        return "RedisConfig{" +
                "jedisPool=" + jedisPool +
                '}';
    }
}
