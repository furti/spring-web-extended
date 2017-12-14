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
package io.github.furti.spring.web.extended.config;

import java.util.List;
import java.util.Map;

import io.github.furti.spring.web.extended.asset.CdnConfig;
import io.github.furti.spring.web.extended.expression.CdnExpressionHandler;
import io.github.furti.spring.web.extended.expression.ExpressionHandler;
import io.github.furti.spring.web.extended.io.ResourceScanner;
import io.github.furti.spring.web.extended.locale.LocaleSource;
import io.github.furti.spring.web.extended.messagesource.MessageSourceConfig;
import io.github.furti.spring.web.extended.template.cache.StackConfig;
import io.github.furti.spring.web.extended.template.optimize.OptimizerConfig;

/**
 * @author Daniel Furtlehner
 * @deprecated use the static folder style instead
 */
@Deprecated
public interface SpringWebExtendedConfigurer
{
    void configureMessageSource(MessageSourceConfig config);

    void configureStyles(StackConfig config);

    void configureScripts(StackConfig config);

    void configureHtmlTemplates(StackConfig config);

    /**
     * Works in combination with the {@link CdnExpressionHandler}. Is espessially usefull if you want to load javascript
     * libraries (e.g. jquery, angular,...) from their CDN location to optimize loading and caching of this resources.
     * 
     * @param config
     */
    void configureCDN(CdnConfig config);

    void configureApplication(ApplicationConfiguration config);

    void configureOptimizers(OptimizerConfig config);

    void configureResourceScanners(Map<String, ResourceScanner> config);

    void configureExpressionHandlers(Map<String, ExpressionHandler> config);

    void configureLocaleSources(List<LocaleSource> sources);
}
