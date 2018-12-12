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

package li.allan.easycache.annotation;

import java.lang.annotation.*;

/**
 * @author LiALuN
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EasyCache {
    /**
     * 命名空间，在多个模块共用一个缓存环境时，避免key冲突
     */
    String namespace() default "";

    String cacheName();

    String key();

    /**
     * 缓存超时时长
     */
    String expired();

    /**
     * 不进行缓存的条件。
     * 通过#result获取返回值。具体参考SpEL表达式。
     */
    String unless() default "";

    /**
     * 缓存最大数量，仅对内部缓存有效
     */
    int maximumSize() default -1;

//    Class<? extends Serializer> serializer() default Serializer.class;
}
