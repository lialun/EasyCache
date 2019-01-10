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

package li.allan.easycache.aspect;

import li.allan.easycache.Cache;
import li.allan.easycache.TestBase;
import li.allan.easycache.annotation.EasyCache;
import li.allan.easycache.config.EasyCacheConfig;
import org.junit.Test;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@SuppressWarnings("WeakerAccess")
@Component
public class EasyCacheTest extends TestBase {
    @Resource
    private EasyCacheTest easyCacheTest;

    @Test
    public void baseMethodTest() {
        assertEquals(easyCacheTest.emptyKey(), easyCacheTest.emptyKey());
    }

    @Test
    public void expireTest() throws InterruptedException {
        String value = easyCacheTest.emptyKey();
        assertEquals(value, easyCacheTest.emptyKey());
        Thread.sleep(1000);
        assertNotEquals(value, easyCacheTest.emptyKey());
    }

    @Test
    public void keyTest() {
        assertEquals(easyCacheTest.base(1), easyCacheTest.base(1));
        assertEquals(easyCacheTest.base(2), easyCacheTest.base(2));
        assertNotEquals(easyCacheTest.base(1), easyCacheTest.base(2));
    }

    @Test
    public void voidReturnTest() {
        easyCacheTest.voidReturn();
        Cache<String, Object> cache = EasyCacheConfig.getLocalCacheOperator().getCache("voidReturn");
        assertNull(cache);
    }

    @Test
    public void sizeTest() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            easyCacheTest.withSize(i);
            easyCacheTest.withoutSize(i);
        }
        Thread.sleep(1000);//wait caffeine cleanup

        Cache<String, Object> cache = EasyCacheConfig.getLocalCacheOperator().getCache("withSize");
        assertEquals(30, cache.size());
        cache = EasyCacheConfig.getLocalCacheOperator().getCache("withoutSize");
        assertEquals(50, cache.size());
    }

    @Test
    public void spELExpiredTest() throws InterruptedException {
        String value = easyCacheTest.spELExpired();
        assertEquals(value, easyCacheTest.spELExpired());
        Thread.sleep(2000);
        assertNotEquals(value, easyCacheTest.emptyKey());
    }

    @Test
    public void withNamespaceTest() throws InterruptedException {
        assertEquals(easyCacheTest.withNamespace(), easyCacheTest.withNamespace());
    }

    @EasyCache(cacheName = "emptyKey", key = "", expired = "1")
    public String emptyKey() {
        return randomString(32);
    }

    @EasyCache(cacheName = "base", key = "#id", expired = "1")
    public String base(int id) {
        return id + randomString(32);
    }

    @EasyCache(cacheName = "withSize", key = "#id", expired = "5s", maximumSize = 30)
    public String withSize(int id) {
        return id + randomString(32);
    }

    @EasyCache(cacheName = "withoutSize", key = "#id", expired = "5s")
    public String withoutSize(int id) {
        return id + randomString(32);
    }

    @EasyCache(cacheName = "voidReturn", key = "", expired = "1")
    public void voidReturn() {

    }

    @EasyCache(cacheName = "spELExpired", key = "", expired = "1*2")
    public String spELExpired() {
        return randomString(32);
    }

    @EasyCache(namespace = "test", cacheName = "spELExpired", key = "", expired = "1*2")
    public String withNamespace() {
        return randomString(32);
    }
}
