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


import li.allan.easycache.LocalCacheConfig;

import java.util.Objects;

/**
 * @author lialun
 */
public class CaffeineConfig implements LocalCacheConfig {
    private int maximumSize = -1;
    private boolean softValues = false;
    private int cleanUpIntervalInSecond = -1;

    public int getMaximumSize() {
        return maximumSize;
    }

    public void setMaximumSize(int maximumSize) {
        this.maximumSize = maximumSize;
    }

    public boolean isSoftValues() {
        return softValues;
    }

    public void setSoftValues(boolean softValues) {
        this.softValues = softValues;
    }

    public int getCleanUpIntervalInSecond() {
        return cleanUpIntervalInSecond;
    }

    public void setCleanUpIntervalInSecond(int cleanUpIntervalInSecond) {
        this.cleanUpIntervalInSecond = cleanUpIntervalInSecond;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CaffeineConfig that = (CaffeineConfig) o;
        return maximumSize == that.maximumSize &&
                softValues == that.softValues &&
                cleanUpIntervalInSecond == that.cleanUpIntervalInSecond;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maximumSize, softValues, cleanUpIntervalInSecond);
    }

    @Override
    public String toString() {
        return "CaffeineConfig{" +
                "maximumSize=" + maximumSize +
                ", softValues=" + softValues +
                ", cleanUpIntervalInSecond=" + cleanUpIntervalInSecond +
                '}';
    }

    public CaffeineConfig clone(int maximumSize) {
        CaffeineConfig caffeineConfig = new CaffeineConfig();
        caffeineConfig.setMaximumSize(maximumSize >= 0 ? maximumSize : getMaximumSize());
        caffeineConfig.setSoftValues(isSoftValues());
        caffeineConfig.setCleanUpIntervalInSecond(getCleanUpIntervalInSecond());
        return caffeineConfig;
    }
}

