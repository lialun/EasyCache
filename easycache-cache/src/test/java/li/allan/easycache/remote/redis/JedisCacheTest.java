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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author lialun
 */
public class JedisCacheTest {

    private static final int TEST_REDIS_PORT = 16394;

    RedisServer redisServer;
    JedisPool jedisPool;
    RedisCache<String, String> jedisCache;

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
        jedisCache.put("key", "value", 1000, TimeUnit.MILLISECONDS,
                StringSerializer.class, Jackson2Serializer.class);

        ValueWrapper<String> value = jedisCache.getValueWrapper("key", StringSerializer.class, Jackson2Serializer.class);
        assertEquals("value", value.getValue());
        assertEquals(jedisCache.get("key", StringSerializer.class, Jackson2Serializer.class), "value");

        Thread.sleep(1000);

        assertNull(jedisCache.getValueWrapper("key", StringSerializer.class, Jackson2Serializer.class));
        assertNull(jedisCache.get("key", StringSerializer.class, Jackson2Serializer.class));
    }

    @Test
    public void get() {
    }

    @Test
    public void getValueWrapper() {
    }

    @Test
    public void contains() {
    }

    @Test
    public void invalidate() {
    }

    @Test
    public void expireTimestampInMills() {
    }

    @Test
    public void size() {
    }
}