/*
 * Copyright (c) 2017-2018. the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package li.allan.easycache.local.caffeine;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * @author lialun
 */
public class CaffeineCacheTest {
    private static final String KEY = "key";
    private static final String VALUE = "value";

    @Test
    public void testCacheSize() throws InterruptedException {
        int cacheSize = 10;
        CaffeineCache<String, String> cache = CaffeineCache.builder().setMaximumSize(cacheSize).build();
        for (int i = 0; i <= 100; i++) {
            cache.put(String.valueOf(i), String.valueOf(i), 1, TimeUnit.MINUTES);
        }
        assertEquals(cacheSize, cache.size());

        int count = 0;
        for (int i = 0; i <= 100; i++) {
            if (cache.get(String.valueOf(i)) != null) {
                count++;
            }
        }
        assertEquals(cacheSize, count);
    }

    @Test
    public void testGet() {
        CaffeineCache<String, String> cache = CaffeineCache.builder().build();

        cache.put(KEY, VALUE, 1, TimeUnit.SECONDS);
        assertEquals(VALUE, cache.get(KEY));

        cache.put("null", null, 1, TimeUnit.SECONDS);
        assertNull(cache.get("null"));

        assertNull(cache.get("noneKey"));
    }

    @Test
    public void testGetValueWrapper() {
        CaffeineCache<String, String> cache = CaffeineCache.builder().build();

        cache.put(KEY, VALUE, 1, TimeUnit.SECONDS);
        Assert.assertEquals(VALUE, cache.getValueWrapper(KEY).getValue());
        assertTrue(Math.abs(cache.getValueWrapper(KEY).getExpireTimestampInMills() - System.currentTimeMillis() - 1000) < 10);

        cache.put("null", null, 1, TimeUnit.SECONDS);
        assertNull(cache.getValueWrapper("null").getValue());

        assertNull(cache.get("noneKey"));
    }

    @Test
    public void testExpire() throws InterruptedException {
        CaffeineCache<String, String> cache = CaffeineCache.builder().build();
        cache.put(KEY, VALUE, 100, TimeUnit.MILLISECONDS);
        assertEquals(cache.get(KEY), VALUE);
        Thread.sleep(100);
        assertNull(cache.get(KEY));
    }

    @Test
    public void testContains() {
        CaffeineCache<String, String> cache = CaffeineCache.builder().build();
        assertFalse(cache.contains(KEY));
        cache.put(KEY, VALUE, 100, TimeUnit.MILLISECONDS);
        assertTrue(cache.contains(KEY));
        assertFalse(cache.contains(KEY + " "));
    }

    @Test
    public void testInvalidate() {
        CaffeineCache<String, String> cache = CaffeineCache.builder().build();
        cache.put(KEY, VALUE, 1, TimeUnit.MINUTES);

        assertTrue(cache.contains(KEY));
        assertEquals(cache.get(KEY), VALUE);

        cache.invalidate(KEY);

        assertFalse(cache.contains(KEY));
        assertNull(cache.get(KEY));
    }

    @Test
    public void testExpireTimestampInMills() {
        CaffeineCache<String, String> cache = CaffeineCache.builder().build();
        cache.put(KEY, VALUE, 1, TimeUnit.SECONDS);
        assertTrue(Math.abs(cache.expireTimestampInMills(KEY) - System.currentTimeMillis() - 1000) < 10);

        assertTrue(cache.expireTimestampInMills(KEY + " ") < 0);
    }

    @Test
    public void testAutoCleanup() throws InterruptedException {
        int cacheSize = 10;
        CaffeineCache<String, String> cache = CaffeineCache.builder()
                .setMaximumSize(cacheSize)
                .enableAutoCleanUp(1).build();
        for (int i = 0; i <= 100; i++) {
            cache.put(String.valueOf(i), String.valueOf(i), 1, TimeUnit.MINUTES);
        }
        //before clean up
        int count = 0;
        for (int i = 0; i <= 100; i++) {
            if (cache.get(String.valueOf(i)) != null) {
                count++;
            }
        }
        assertTrue(cacheSize < count);

        Thread.sleep(2000);
        //after auto clean up
        count = 0;
        for (int i = 0; i <= 100; i++) {
            if (cache.get(String.valueOf(i)) != null) {
                count++;
            }
        }
        assertEquals(cacheSize, count);
    }
}