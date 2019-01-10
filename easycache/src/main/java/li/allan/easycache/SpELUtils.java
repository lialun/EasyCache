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

import li.allan.easycache.logging.Log;
import li.allan.easycache.logging.LogFactory;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

/**
 * @author LiALuN
 */
public class SpELUtils {
    private static Log log = LogFactory.getLog(SpELUtils.class);

    public static boolean condition(Map<String, Object> variables, String spEL) {
        return getValue(variables, spEL, Boolean.class);
    }

    public static <T> T getValue(Map<String, Object> variables, String spEL, Class<T> valueType) {
        return getValue(null, variables, spEL, valueType);
    }

    public static <T> T getValue(Object rootObject, Map<String, Object> variables, String spEL, Class<T> valueType) {
        StandardEvaluationContext context = new StandardEvaluationContext(rootObject);
        if (variables != null) {
            context.setVariables(variables);
        }
        return getValue(context, spEL, valueType);
    }

    public static <T> T getValue(StandardEvaluationContext context, String spEL, Class<T> valueType) {
        ExpressionParser parser = new SpelExpressionParser();
        return parser.parseExpression(spEL).getValue(context, valueType);
    }

    private static final StandardEvaluationContext TIME_CONTEXT = new StandardEvaluationContext();

    static {
        try {
            TIME_CONTEXT.registerFunction("now", TimeUtils.class.getDeclaredMethod("now"));
            TIME_CONTEXT.registerFunction("today", TimeUtils.class.getDeclaredMethod("today"));
            TIME_CONTEXT.registerFunction("tomorrow", TimeUtils.class.getDeclaredMethod("tomorrow"));
            TIME_CONTEXT.registerFunction("diffTime", TimeUtils.class.getDeclaredMethod("diffTime", TimeUtils.CalendarWrapper.class, TimeUtils.CalendarWrapper.class));

        } catch (NoSuchMethodException e) {
            log.error("expire time SpEL Method INIT ERROR", e);
        }
    }

    public static int getTime(String SpEL) {
        return getValue(TIME_CONTEXT, SpEL, Integer.class);
    }
}
