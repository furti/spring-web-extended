/**
 * Copyright 2014 Daniel Furtlehner Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package at.porscheinformatik.common.spring.web.extended.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;

import at.porscheinformatik.common.spring.web.extended.asset.CdnConfig;
import at.porscheinformatik.common.spring.web.extended.asset.DefaultCdnConfig;
import at.porscheinformatik.common.spring.web.extended.expression.ExpressionHandler;
import at.porscheinformatik.common.spring.web.extended.io.ResourceScanner;
import at.porscheinformatik.common.spring.web.extended.io.ResourceType;
import at.porscheinformatik.common.spring.web.extended.locale.LocaleSource;
import at.porscheinformatik.common.spring.web.extended.locale.RequestURILocaleSource;
import at.porscheinformatik.common.spring.web.extended.messagesource.DefaultMessageSourceConfig;
import at.porscheinformatik.common.spring.web.extended.messagesource.MessageSourceConfig;
import at.porscheinformatik.common.spring.web.extended.template.cache.DefaultStackConfig;
import at.porscheinformatik.common.spring.web.extended.template.optimize.DefaultOptimizerConfig;
import at.porscheinformatik.common.spring.web.extended.template.optimize.OptimizerConfig;
import ro.isdc.wro.extensions.processor.css.YUICssCompressorProcessor;
import ro.isdc.wro.model.resource.processor.impl.js.JSMinProcessor;
import ro.isdc.wro.util.Base64;

/**
 * Contains all the functionallity needed for handling the {@link SpringWebExtendedConfigurer}s
 *
 * @author Daniel Furtlehner
 */
@Configuration
public class SpringWebExtendedConfigurerConfig
{
    private static final Integer DEFAULT_REFRESH_INTERVALL = Integer.valueOf(5);

    @Autowired
    private Environment environment;

    private final DelegatingSpringWebExtendedConfiguerer configurer = new DelegatingSpringWebExtendedConfiguerer();

    private DefaultStackConfig scriptConfig, styleConfig, htmlConfig;
    private OptimizerConfig optimizerConfig;
    private CdnConfig cdnConfig;

    @Autowired(required = false)
    public void setConfigurers(List<SpringWebExtendedConfigurer> configurers)
    {
        configurer.addConfigurers(configurers);
    }

    public DefaultApplicationConfiguration appConfig()
    {
        DefaultApplicationConfiguration config = new DefaultApplicationConfiguration();

        // Set the default optimize flag
        config.setOptimizeResources(environment.acceptsProfiles("optimizeresources"));

        config.addLocale("en");

        configurer.configureApplication(config);

        if (config.getVersion() == null)
        {
            byte[] bytes = new byte[15];
            new Random().nextBytes(bytes);

            try
            {
                config.setVersion(Base64.encodeBytes(bytes, Base64.URL_SAFE));
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }

        return config;
    }

    @Bean
    public LocaleSource requestUriLocaleSource()
    {
        return new RequestURILocaleSource();
    }

    public DefaultStackConfig getStyleConfig()
    {
        if (styleConfig == null)
        {
            ApplicationConfiguration appConfig = appConfig();

            styleConfig = new DefaultStackConfig();
            // Default refresh intervall
            styleConfig.setRefreshIntervall(appConfig.isOptimizeResources() ? -1 : DEFAULT_REFRESH_INTERVALL);

            configurer.configureStyles(styleConfig);
        }

        return styleConfig;
    }

    public DefaultStackConfig getHtmlConfig()
    {
        if (htmlConfig == null)
        {
            ApplicationConfiguration appConfig = appConfig();

            htmlConfig = new DefaultStackConfig();
            htmlConfig.setRefreshIntervall(appConfig.isOptimizeResources() ? -1 : DEFAULT_REFRESH_INTERVALL);

            htmlConfig.scanPattern("", "templates/**/*.html");

            configurer.configureHtmlTemplates(htmlConfig);
        }

        return htmlConfig;
    }

    public CdnConfig getCdnConfig()
    {
        if (cdnConfig == null)
        {
            cdnConfig = new DefaultCdnConfig();

            configurer.configureCDN(cdnConfig);
        }

        return cdnConfig;
    }

    public DefaultStackConfig getScriptConfig()
    {
        if (scriptConfig == null)
        {
            ApplicationConfiguration appConfig = appConfig();

            scriptConfig = new DefaultStackConfig();
            // Default refresh intervall
            scriptConfig.setRefreshIntervall(appConfig.isOptimizeResources() ? -1 : DEFAULT_REFRESH_INTERVALL);

            configurer.configureScripts(scriptConfig);
        }

        return scriptConfig;
    }

    public OptimizerConfig getOptimizerConfig()
    {
        if (optimizerConfig == null)
        {

            optimizerConfig = new DefaultOptimizerConfig();

            /*
             * We have to use the yuicompressor here because the others don't
             * recognize @media queries
             * https://code.google.com/p/wro4j/issues/detail?id=231
             */
            optimizerConfig.addOptimizer(ResourceType.STYLE, "yuicss", new YUICssCompressorProcessor());
            optimizerConfig.addOptimizer(ResourceType.SCRIPT, "jsmin", new JSMinProcessor());

            configurer.configureOptimizers(optimizerConfig);
        }

        return optimizerConfig;
    }

    public void configureMessageSource(ReloadableResourceBundleMessageSource messageSource)
    {
        ApplicationConfiguration appConfig = appConfig();

        MessageSourceConfig config = new DefaultMessageSourceConfig();
        config.addBaseName("WEB-INF/messages/Messages");
        config.setCacheSeconds(appConfig.isOptimizeResources() ? -1 : DEFAULT_REFRESH_INTERVALL);
        config.setDefaultEncoding("UTF-8");

        configurer.configureMessageSource(config);

        messageSource.setCacheSeconds(config.getCacheSeconds() != null ? config.getCacheSeconds() : -1);

        if (!CollectionUtils.isEmpty(config.getBaseNames()))
        {
            messageSource.setBasenames(config.getBaseNames().toArray(new String[config.getBaseNames().size()]));
        }

        messageSource.setDefaultEncoding(config.getDefaultEncoding());
    }

    public List<LocaleSource> getLocaleSources()
    {
        List<LocaleSource> sources = new ArrayList<>();

        sources.add(requestUriLocaleSource());
        configurer.configureLocaleSources(sources);

        return sources;
    }

    public void configureResourceScanners(Map<String, ResourceScanner> scanners)
    {
        configurer.configureResourceScanners(scanners);
    }

    public void configureExpressionHandlers(HashMap<String, ExpressionHandler> handlers)
    {
        configurer.configureExpressionHandlers(handlers);
    }
}
