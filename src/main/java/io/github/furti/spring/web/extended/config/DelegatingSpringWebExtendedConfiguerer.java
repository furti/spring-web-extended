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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import io.github.furti.spring.web.extended.asset.CdnConfig;
import io.github.furti.spring.web.extended.expression.legacy.ExpressionHandler;
import io.github.furti.spring.web.extended.io.ResourceScanner;
import io.github.furti.spring.web.extended.locale.LocaleSource;
import io.github.furti.spring.web.extended.messagesource.MessageSourceConfig;
import io.github.furti.spring.web.extended.template.legacy.cache.StackConfig;
import io.github.furti.spring.web.extended.template.legacy.optimize.OptimizerConfig;

/**
 * @author Daniel Furtlehner
 */
public class DelegatingSpringWebExtendedConfiguerer implements SpringWebExtendedConfigurer
{

    private final List<SpringWebExtendedConfigurer> configurers = new ArrayList<>();

    public void addConfigurers(Collection<SpringWebExtendedConfigurer> configurers)
    {
        if (configurers != null)
        {
            this.configurers.addAll(configurers);
        }
    }

    @Override
    public void configureMessageSource(MessageSourceConfig config)
    {
        for (SpringWebExtendedConfigurer configurer : configurers)
        {
            configurer.configureMessageSource(config);
        }
    }

    @Override
    public void configureStyles(StackConfig config)
    {
        for (SpringWebExtendedConfigurer configurer : configurers)
        {
            configurer.configureStyles(config);
        }
    }

    @Override
    public void configureScripts(StackConfig config)
    {
        for (SpringWebExtendedConfigurer configurer : configurers)
        {
            configurer.configureScripts(config);
        }
    }

    @Override
    public void configureApplication(ApplicationConfiguration config)
    {
        for (SpringWebExtendedConfigurer configurer : configurers)
        {
            configurer.configureApplication(config);
        }
    }

    @Override
    public void configureOptimizers(OptimizerConfig config)
    {
        for (SpringWebExtendedConfigurer configurer : configurers)
        {
            configurer.configureOptimizers(config);
        }
    }

    @Override
    public void configureResourceScanners(Map<String, ResourceScanner> config)
    {
        for (SpringWebExtendedConfigurer configurer : configurers)
        {
            configurer.configureResourceScanners(config);
        }
    }

    @Override
    public void configureExpressionHandlers(Map<String, ExpressionHandler> config)
    {
        for (SpringWebExtendedConfigurer configurer : configurers)
        {
            configurer.configureExpressionHandlers(config);
        }
    }

    @Override
    public void configureHtmlTemplates(StackConfig config)
    {
        for (SpringWebExtendedConfigurer configurer : configurers)
        {
            configurer.configureHtmlTemplates(config);
        }
    }

    /**
     * @param sources
     */
    @Override
    public void configureLocaleSources(List<LocaleSource> sources)
    {
        for (SpringWebExtendedConfigurer configurer : configurers)
        {
            configurer.configureLocaleSources(sources);
        }
    }

    @Override
    public void configureCDN(CdnConfig config)
    {
        for (SpringWebExtendedConfigurer configurer : configurers)
        {
            configurer.configureCDN(config);
        }
    }
}
