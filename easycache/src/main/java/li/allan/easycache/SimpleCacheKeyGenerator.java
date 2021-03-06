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

package li.allan.easycache;

import com.google.common.base.Strings;
import li.allan.easycache.aspect.MethodParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lialun
 */
public class SimpleCacheKeyGenerator extends CacheKeyGenerator {

    @Override
    public String cacheName(String namespace, String cacheName) {
        if (Strings.isNullOrEmpty(namespace)) {
            return cacheName;
        }
        return namespace + SEPARATOR + cacheName;
    }

    @Override
    public String cacheKey(String key, List<MethodParam> methodParams) {
        if (Strings.isNullOrEmpty(key)) {
            return "";
        }
        Map<String, Object> variables = methodParams.stream()
                .collect(Collectors.toMap(MethodParam::getName, MethodParam::getValue));
        return SpELUtils.getValue(variables, key, String.class);
    }
}
