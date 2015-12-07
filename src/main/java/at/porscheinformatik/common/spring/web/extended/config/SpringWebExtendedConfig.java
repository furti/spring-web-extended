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
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import at.porscheinformatik.common.spring.web.extended.io.LocalizedResourceLoader;
import at.porscheinformatik.common.spring.web.extended.io.LocalizedResourceLoaderImpl;
import at.porscheinformatik.common.spring.web.extended.io.ResourceScanners;
import at.porscheinformatik.common.spring.web.extended.io.ResourceUtils;
import at.porscheinformatik.common.spring.web.extended.locale.LocaleContextHolderBackedLocaleContext;
import at.porscheinformatik.common.spring.web.extended.locale.LocaleHandlerInterceptor;
import at.porscheinformatik.common.spring.web.extended.locale.LocaleSource;
import at.porscheinformatik.common.spring.web.extended.servlet.RequestResponseContextHandlerInterceptor;
import at.porscheinformatik.common.spring.web.extended.template.cache.DefaultStackConfig;
import at.porscheinformatik.common.spring.web.extended.template.cache.StackConfig;
import at.porscheinformatik.common.spring.web.extended.template.cache.html.HtmlStacks;
import at.porscheinformatik.common.spring.web.extended.template.cache.script.ScriptStacks;
import at.porscheinformatik.common.spring.web.extended.template.cache.style.StyleStacks;
import at.porscheinformatik.common.spring.web.extended.template.optimize.OptimizerChain;

@Configuration
@EnableScheduling
@EnableWebMvc
@Import(value = {ResourceScannerConfig.class, ExpressionHandlerConfig.class, SpringWebExtendedConfigurerConfig.class})
// TODO: maybe we should add a handlerinterceptor that adds no-cache headers
// for json responses. Spring security adds this headers by default. So we can
// skip this i think
public class SpringWebExtendedConfig extends WebMvcConfigurerAdapter implements
    SchedulingConfigurer
{
    @Autowired
    private SpringWebExtendedConfigurerConfig configurerConfig;

    @Autowired
    private ResourceScannerConfig scannerConfig;

    @Bean
    public MessageSource messageSource()
    {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setFallbackToSystemLocale(false);

        configurerConfig.configureMessageSource(messageSource);

        return messageSource;
    }

    @Bean
    public LocaleContext localeContext()
    {
        return new LocaleContextHolderBackedLocaleContext();
    }

    @Bean
    public LocalizedResourceLoader localizedResourceLoader()
    {
        return new LocalizedResourceLoaderImpl();
    }

    @Bean
    public ApplicationConfiguration appConfig() throws IOException
    {
        DefaultApplicationConfiguration appConfig = configurerConfig.appConfig();
        setupScannedLocales(appConfig);
        return appConfig;
    }

    @Bean
    public StyleStacks styleStacks()
    {
        DefaultStackConfig config = configurerConfig.getStyleConfig();

        StyleStacks styleStacks = new StyleStacks(config);
        styleStacks.setAppConfig(configurerConfig.appConfig());
        styleStacks.setOptimizerChain(optimizerChain());
        styleStacks.setScanners(scannerConfig.resourceScanners());
        return styleStacks;
    }

    @Bean
    public ScriptStacks scriptStacks()
    {
        DefaultStackConfig config = configurerConfig.getScriptConfig();
        ScriptStacks scriptStacks = new ScriptStacks(config);
        scriptStacks.setAppConfig(configurerConfig.appConfig());
        scriptStacks.setOptimizerChain(optimizerChain());
        scriptStacks.setScanners(scannerConfig.resourceScanners());
        return scriptStacks;
    }

    @Bean
    public HtmlStacks htmlStacks()
    {
        DefaultStackConfig config = configurerConfig.getHtmlConfig();

        HtmlStacks htmlStacks = new HtmlStacks(config);
        htmlStacks.setAppConfig(configurerConfig.appConfig());
        htmlStacks.setOptimizerChain(optimizerChain());
        htmlStacks.setScanners(scannerConfig.resourceScanners());
        return htmlStacks;
    }

    @Bean
    public OptimizerChain optimizerChain()
    {
        return new OptimizerChain(configurerConfig.getOptimizerConfig());
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar)
    {
        StackConfig htmlConfig = configurerConfig.getHtmlConfig();
        final HtmlStacks htmlStacks = htmlStacks();

        if (htmlConfig.getRefreshIntervall() > 0
            && htmlStacks != null)
        {
            taskRegistrar.addFixedDelayTask(
                new Runnable()
                {

                    @Override
                    public void run()
                    {
                        htmlStacks.refresh();
                    }
                },
                htmlConfig.getRefreshIntervall() * 1000);
        }

        StackConfig styleConfig = configurerConfig.getStyleConfig();
        final StyleStacks styles = styleStacks();

        if (styleConfig.getRefreshIntervall() > 0
            && styles != null)
        {

            taskRegistrar.addFixedDelayTask(
                new Runnable()
                {

                    @Override
                    public void run()
                    {
                        styles.refresh();
                    }
                },
                styleConfig.getRefreshIntervall() * 1000);
        }

        StackConfig scriptConfig = configurerConfig.getScriptConfig();
        final ScriptStacks scripts = scriptStacks();

        if (scriptConfig.getRefreshIntervall() > 0
            && scripts != null)
        {

            taskRegistrar.addFixedDelayTask(
                new Runnable()
                {

                    @Override
                    public void run()
                    {
                        scripts.refresh();
                    }
                },
                scriptConfig.getRefreshIntervall() * 1000);
        }
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(new RequestResponseContextHandlerInterceptor());

        List<LocaleSource> sources = configurerConfig.getLocaleSources();

        if (!CollectionUtils.isEmpty(sources))
        {
            LocaleHandlerInterceptor interceptor = new LocaleHandlerInterceptor(
                sources);
            interceptor.setAvailableLocales(
                configurerConfig.appConfig().getSupportedLocales());

            registry.addInterceptor(interceptor);
        }
    }

    private void setupScannedLocales(DefaultApplicationConfiguration appConfig) throws IOException
    {
        if (appConfig.getMessagesToScan().isEmpty())
        {
            return;
        }

        //We scan the messages directory for all locales that are translated.
        ResourceScanners resourceScanners = scannerConfig.resourceScanners();

        for (String path : appConfig.getMessagesToScan())
        {

            Map<String, Resource> messages = resourceScanners.scanResources(path);

            if (messages == null || messages.isEmpty())
            {
                throw new RuntimeException("No Messages found for supported locales");
            }
            else
            {
                for (String messageName : messages.keySet())
                {
                    String localeString =
                        ResourceUtils.getLocaleFromName(ResourceUtils.getNameAndEnding(messageName)[0]);

                    if (localeString != null)
                    {
                        String[] localeParts = localeString.split("_");
                        Locale locale = null;

                        if (localeParts.length == 3)
                        {
                            locale = new Locale(localeParts[0], localeParts[1], localeParts[2]);
                        }
                        else if (localeParts.length == 2)
                        {
                            locale = new Locale(localeParts[0], localeParts[1]);
                        }
                        else
                        {
                            locale = new Locale(localeParts[0]);
                        }

                        if (locale != null && !appConfig.getRawSupportedLocales().contains(locale))
                        {
                            appConfig.getRawSupportedLocales().add(locale);
                        }
                    }
                }
            }
        }
    }
}
