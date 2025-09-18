/**
 * Copyright 2014 Daniel Furtlehner
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
package io.github.furti.spring.web.extended.messagesource;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Daniel Furtlehner
 */
public class DefaultMessageSourceConfig implements MessageSourceConfig {

    private Integer cacheSeconds;
    private final Set<String> baseNames;
    private String defaultEncoding;

    public DefaultMessageSourceConfig() {
        baseNames = new HashSet<>();
    }

    @Override
    public Integer getCacheSeconds() {
        return cacheSeconds;
    }

    @Override
    public Set<String> getBaseNames() {
        return baseNames;
    }

    @Override
    public void addBaseName(String baseName) {
        baseNames.add(baseName);
    }

    @Override
    public void removeBaseName(String baseName) {
        baseNames.remove(baseName);
    }

    @Override
    public void setCacheSeconds(Integer seconds) {
        cacheSeconds = seconds;
    }

    @Override
    public String getDefaultEncoding() {
        return defaultEncoding;
    }

    @Override
    public void setDefaultEncoding(String defaultEncoding) {
        this.defaultEncoding = defaultEncoding;
    }
}
