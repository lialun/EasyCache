package li.allan.easycache.remote.redis;

import li.allan.easycache.ValueWrapper;
import li.allan.easycache.remote.serializer.Jackson2Serializer;
import li.allan.easycache.remote.serializer.StringSerializer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.JedisPool;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * @author lialun
 */
public class JedisCacheTest {

    private static final int TEST_REDIS_PORT = 16394;

    private static final String KEY = "key";
    private static final String VALUE = "value";

    private RedisServer redisServer;
    private JedisPool jedisPool;
    private RedisCache<String, String> jedisCache;

    @Before
    public void startRedis() throws IOException {
        redisServer = new RedisServer(TEST_REDIS_PORT);
        redisServer.start();

        jedisPool = new JedisPool("localhost", TEST_REDIS_PORT);

        jedisCache = RedisCache.builder().setJedisPool(jedisPool).build();
    }

    @After
    public void stopRedis() {
        redisServer.stop();
    }

    @Test
    public void putAndGet() throws InterruptedException {
        jedisCache.put(KEY, VALUE, 1000, TimeUnit.MILLISECONDS,
                StringSerializer.class, Jackson2Serializer.class);

        ValueWrapper<String> value = jedisCache.getValueWrapper(KEY, StringSerializer.class, Jackson2Serializer.class);
        assertEquals(VALUE, value.getValue());
        assertEquals(jedisCache.get(KEY, StringSerializer.class, Jackson2Serializer.class), VALUE);

        Thread.sleep(1000);

        assertNull(jedisCache.getValueWrapper(KEY, StringSerializer.class, Jackson2Serializer.class));
        assertNull(jedisCache.get(KEY, StringSerializer.class, Jackson2Serializer.class));
    }

    @Test
    public void containsAndInvalidate() {
        jedisCache.invalidate(KEY, StringSerializer.class);
        assertFalse(jedisCache.contains(KEY, StringSerializer.class));

        jedisCache.put(KEY, VALUE, 1000, TimeUnit.MILLISECONDS,
                StringSerializer.class, Jackson2Serializer.class);
        assertTrue(jedisCache.contains(KEY, StringSerializer.class));
    }

    @Test
    public void expireTimestampInMills() {
        jedisCache.put(KEY, VALUE, 1, TimeUnit.SECONDS, StringSerializer.class, Jackson2Serializer.class);
        assertTrue(Math.abs(jedisCache.expireTimestampInMills(KEY, StringSerializer.class) - System.currentTimeMillis() - 1000) < 10);

        assertTrue(jedisCache.expireTimestampInMills(KEY + " ", StringSerializer.class) < 0);
    }
}