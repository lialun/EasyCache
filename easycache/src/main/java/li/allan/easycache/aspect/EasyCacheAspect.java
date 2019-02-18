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

import com.google.common.base.Strings;
import com.google.common.primitives.Longs;
import li.allan.easycache.ValueWrapper;
import li.allan.easycache.annotation.EasyCache;
import li.allan.easycache.cache.CacheOperator;
import li.allan.easycache.config.EasyCacheConfig;
import li.allan.easycache.exception.EasyCacheELIllegalException;
import li.allan.easycache.logging.Log;
import li.allan.easycache.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static li.allan.easycache.DurationUtils.match;
import static li.allan.easycache.DurationUtils.parseToSeconds;
import static li.allan.easycache.SpELUtils.condition;
import static li.allan.easycache.SpELUtils.getTime;


/**
 * @author LiALuN
 */
@Aspect
@Component
public class EasyCacheAspect {
    private Log log = LogFactory.getLog(EasyCacheAspect.class);

    @Around("@annotation(easyCache)")
    public Object cacheMethod(final ProceedingJoinPoint point, final EasyCache easyCache) throws Throwable {
        /*
         * get all params for config method
         */
        String className = point.getTarget().getClass().getSimpleName();
        Method method = getMethodFromProceedingJoinPoint(point);
        Class returnType = method.getReturnType();
        Class valueSerializer = easyCache.valueSerializer()
        //method return void don't need config
        if (returnType.equals(Void.TYPE)) {
            return point.proceed();
        }
        List<MethodParam> methodParams = getParamsFromMethod(method, point.getArgs());
        log.debug("Annotation EasyCache method start, proceeding join point at " + className + "." + method.getName());
        /*
         * generate config name and key
         */
        //TODO 优化key生成方法传入的参数，使得Generator能够支持更多cacheName、cacheKey生成凡是
        String cacheName = EasyCacheConfig.getCacheKeyGenerator().cacheName(easyCache.namespace(), easyCache.cacheName());
        String cacheKey = EasyCacheConfig.getCacheKeyGenerator().cacheKey(easyCache.key(), methodParams);
        log.debug("Generate cacheName = \"" + cacheName + "\", cacheKey = \"" + cacheKey + "\"");
        /*
         * try to get config
         */
        try {
            log.debug("Try to get config");
            ValueWrapper cache = CacheOperator.get(easyCache.cacheType(), cacheName, cacheKey, easyCache.valueSerializer(), returnType);
            if (cache != null) {
                return cache.getValue();
            }
        } catch (Exception e) {
            log.error("EasyCache get data from cache failure", e);
        }
        /*
         * invoke method
         */
        log.debug("Invoke origin method");
        Object resp = point.proceed();
        /*
         * save config data
         */
        try {
            if (!unless(easyCache.unless(), resp, methodParams)) {
                log.debug("Try to set config");
                long expireTime = expireTimeInSecond(easyCache.expired());
                if (expireTime > 0) {
                    CacheOperator.put(easyCache.cacheType(), cacheName, cacheKey, resp, easyCache.valueSerializer(), expireTime, easyCache.maximumSize());
                }
            }
        } catch (Exception e) {
            log.error("EasyCache set config ERROR", e);
        }
        return resp;
    }

    private List<MethodParam> getParamsFromMethod(Method method, Object[] args) {
        List<MethodParam> list = new ArrayList<>();
        LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] paraNames = discoverer.getParameterNames(method);
        for (int i = 0; i < args.length; i++) {
            list.add(new MethodParam(paraNames[i], args[i]));
        }
        return list;
    }

    private static Method getMethodFromProceedingJoinPoint(ProceedingJoinPoint point) throws NoSuchMethodException {
        //get method from MethodSignature
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        // signature.getMethod() will return the method of the interface,
        // be sure to get the method of the implementation class
        if (method.getDeclaringClass().isInterface()) {
            method = point.getTarget().getClass().getDeclaredMethod(point.getSignature().getName(),
                    method.getParameterTypes());
        }
        return method;
    }

    private boolean unless(String unless, Object result, List<MethodParam> params) {
        if (Strings.isNullOrEmpty(unless)) {
            return false;
        }
        try {
            Map<String, Object> variables = new HashMap<>(
                    params.stream().collect(Collectors.toMap(MethodParam::getName, MethodParam::getValue))
            );
            variables.put("result", result);
            return condition(variables, unless);
        } catch (SpelEvaluationException e) {
            throw new EasyCacheELIllegalException("unless EL illegal: " + unless, e);
        }
    }

    /**
     * 计算缓存过期时间
     */
    private long expireTimeInSecond(String expired) {
        if (!Strings.isNullOrEmpty(expired)) {
            try {
                Long expireTimeInSeconds = Longs.tryParse(expired);
                if (expireTimeInSeconds != null) {
                    return expireTimeInSeconds;
                } else if (match(expired)) {
                    return parseToSeconds(expired);
                } else {
                    return getTime(expired);
                }
            } catch (RuntimeException e) {
                log.error("expire illegal" + expired, e);
            }
        }
        return 0;
    }
}
