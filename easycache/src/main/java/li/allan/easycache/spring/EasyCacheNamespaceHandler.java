/*
 * Copyright  2017-2018. the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package li.allan.easycache.spring;

import li.allan.easycache.LocalCacheConfig;
import li.allan.easycache.aspect.EasyCacheAspect;
import li.allan.easycache.config.ConfigProperties;
import li.allan.easycache.config.EasyCacheConfig;
import li.allan.easycache.local.caffeine.CaffeineConfig;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/**
 * @author LiALuN
 */
public class EasyCacheNamespaceHandler extends NamespaceHandlerSupport {
    private static final String CAFFEINE = "caffeine";

    @Override
    public void init() {
        this.registerBeanDefinitionParser("annotation_cache", new AnnotationCacheParser());
        this.registerBeanDefinitionParser("config", new EasyCacheConfigPropertiesParser());
    }

    private static class AnnotationCacheParser extends AbstractBeanDefinitionParser {

        @Override
        protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) throws BeanDefinitionStoreException {
            return "easyCacheAspect";
        }

        @Override
        protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
            BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(EasyCacheAspect.class);
            return factory.getBeanDefinition();
        }
    }

    private static class EasyCacheConfigPropertiesParser extends AbstractBeanDefinitionParser {

        @Override
        protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) throws BeanDefinitionStoreException {
            return "easyCacheConfig";
        }

        @Override
        protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
            return parseParentElement(element);
        }

        private static AbstractBeanDefinition parseParentElement(Element element) {
            BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(ConfigProperties.class);
            ConfigProperties configProperties = new ConfigProperties();

            LocalCacheConfig localCacheConfig = parseLocalCache(DomUtils.getChildElementByTagName(element, "local_cache"));
            if (localCacheConfig != null) {
                factory.addPropertyValue("localCacheConfig", localCacheConfig);
                configProperties.setLocalCacheConfig(localCacheConfig);
            }

//            parseSerializer(element, factory);
//            parseOthers(element, factory);
            EasyCacheConfig.init(configProperties);
            return factory.getBeanDefinition();
        }

        private static LocalCacheConfig parseLocalCache(Element localCacheElement) {
            if (localCacheElement == null) {
                return null;
            }
            String cacheType = localCacheElement.getAttribute("cache_type");
            if (CAFFEINE.equals(cacheType)) {
                return parseCaffeine(localCacheElement);
            }
            return null;
        }

        private static CaffeineConfig parseCaffeine(Element localCacheElement) {
            CaffeineConfig caffeineConfig = new CaffeineConfig();

            Element maximumSize = DomUtils.getChildElementByTagName(localCacheElement, "maximum_size_per_cachekey");
            if (maximumSize != null) {
                caffeineConfig.setMaximumSize(Integer.parseInt(maximumSize.getAttribute("default")));
            }

            Element softValue = DomUtils.getChildElementByTagName(localCacheElement, "cache_soft_value");
            if (softValue != null) {
                caffeineConfig.setSoftValues(Boolean.parseBoolean(softValue.getAttribute("value")));
            }

            Element cleanupInterval = DomUtils.getChildElementByTagName(localCacheElement, "cleanup_interval");
            if (cleanupInterval != null) {
                caffeineConfig.setCleanUpIntervalInSecond(Integer.parseInt(cleanupInterval.getAttribute("seconds")));
            }
            return caffeineConfig;
        }

//        private static void parseCache(Element element, BeanDefinitionBuilder factory) {
//            /*
//             * internal Cache Config
//             */
//            Element externalElement = DomUtils.getChildElementByTagName(element, "external");
//            if (externalElement != null) {
//                Element cacheElement = DomUtils.getChildElements(externalElement).get(0);
//                if (cacheElement.getLocalName().equals("redis")) {
//                    parseRedis(cacheElement, factory);
//                }
//            }
//            /*
//             * default Cache Config
//             */
//            List<Element> defaultCacheElements = DomUtils.getChildElementsByTagName(element, "defaultCache");
//            if (defaultCacheElements.size() > 0) {
//                Element defaultCacheElement = defaultCacheElements.get(0);
//                factory.addPropertyValue("defaultCache", CacheType.valueOf(defaultCacheElement.getTextContent()));
//            }
//        }
//
//        private static void parseRedis(Element element, BeanDefinitionBuilder factory) {
//            BeanDefinitionBuilder component = BeanDefinitionBuilder.rootBeanDefinition(RedisConfig.class);
//            String poolConfigRef = DomUtils.getChildElementByTagName(element, "pool").getAttribute("ref");
//            component.addPropertyReference("jedisPool", poolConfigRef);
//            factory.addPropertyValue("externalCacheConfig", component.getBeanDefinition());
//        }

//        private static void parseBackup(Element element, BeanDefinitionBuilder factory) {
//            List<Element> backupElements = DomUtils.getChildElementsByTagName(element, "backup");
//            if (backupElements.size() == 0) {
//                return;
//            }
//            BeanDefinitionBuilder component = BeanDefinitionBuilder.rootBeanDefinition(ExpireMapConfig.class);
//            if (!Strings.isNullOrEmpty(backupElements.get(0).getAttribute("size"))) {
//                component.addPropertyValue("maxSize", backupElements.get(0).getAttribute("size"));
//            }
//            factory.addPropertyValue("backupCacheConfig", component.getBeanDefinition());
//        }

//        private static void parseSerializer(Element element, BeanDefinitionBuilder factory) {
//            //keySerializer
//            List<Element> keySerializerElements = DomUtils.getChildElementsByTagName(element, "keySerializer");
//            if (keySerializerElements.size() != 0 && !Strings.isNullOrEmpty(keySerializerElements.get(0).getAttribute("class"))) {
//                try {
//                    Class cls = Class.forName(keySerializerElements.get(0).getAttribute("class"));
//                    factory.addPropertyValue("keySerializer", cls);
//                } catch (Exception e) {
//                    log.error("EasyCache config keySerializer Illegal", e);
//                }
//            }
//            //valueSerializer
//            List<Element> valueSerializerElements = DomUtils.getChildElementsByTagName(element, "valueSerializer");
//            if (valueSerializerElements.size() != 0 && !Strings.isNullOrEmpty(valueSerializerElements.get(0).getAttribute("class"))) {
//                try {
//                    Class cls = Class.forName(valueSerializerElements.get(0).getAttribute("class"));
//                    factory.addPropertyValue("valueSerializer", cls);
//                } catch (Exception e) {
//                    log.error("EasyCache config keySerializer Illegal", e);
//                }
//            }
//        }

//        private static void parseOthers(Element element, BeanDefinitionBuilder factory) {
//            //cacheExpire
//            List<Element> cacheExpireElements = DomUtils.getChildElementsByTagName(element, "defaultCacheExpire");
//            if (cacheExpireElements.size() == 0) {
//                return;
//            }
//            if (!Strings.isNullOrEmpty(cacheExpireElements.get(0).getAttribute("seconds"))) {
//                factory.addPropertyValue("defaultCacheExpire", cacheExpireElements.get(0).getAttribute("seconds"));
//            }
//        }
    }
}
