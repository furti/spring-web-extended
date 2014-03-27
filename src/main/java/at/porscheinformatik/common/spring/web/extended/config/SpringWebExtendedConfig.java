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
package at.porscheinformatik.common.spring.web.extended.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import ro.isdc.wro.extensions.processor.css.YUICssCompressorProcessor;
import ro.isdc.wro.model.resource.processor.impl.js.JSMinProcessor;
import ro.isdc.wro.util.Base64;
import at.porscheinformatik.common.spring.web.extended.expression.AssetExpressionHandler;
import at.porscheinformatik.common.spring.web.extended.expression.ExpressionHandler;
import at.porscheinformatik.common.spring.web.extended.expression.ExpressionHandlers;
import at.porscheinformatik.common.spring.web.extended.expression.InlineTemplateExpressionHandler;
import at.porscheinformatik.common.spring.web.extended.expression.MessageExpressionHandler;
import at.porscheinformatik.common.spring.web.extended.expression.ScriptExpressionHandler;
import at.porscheinformatik.common.spring.web.extended.expression.StyleExpressionHandler;
import at.porscheinformatik.common.spring.web.extended.io.AngularResourceScanner;
import at.porscheinformatik.common.spring.web.extended.io.AngularUiBootstrapResourceScanner;
import at.porscheinformatik.common.spring.web.extended.io.BootstrapCssResourceScanner;
import at.porscheinformatik.common.spring.web.extended.io.BootstrapJsResourceScanner;
import at.porscheinformatik.common.spring.web.extended.io.ClasspathResourceScanner;
import at.porscheinformatik.common.spring.web.extended.io.ContextResourceScanner;
import at.porscheinformatik.common.spring.web.extended.io.JqueryResourceScanner;
import at.porscheinformatik.common.spring.web.extended.io.LocalizedResourceLoader;
import at.porscheinformatik.common.spring.web.extended.io.LocalizedResourceLoaderImpl;
import at.porscheinformatik.common.spring.web.extended.io.ResourceScanner;
import at.porscheinformatik.common.spring.web.extended.io.ResourceScanners;
import at.porscheinformatik.common.spring.web.extended.io.ResourceType;
import at.porscheinformatik.common.spring.web.extended.io.WebJarResourceScanner;
import at.porscheinformatik.common.spring.web.extended.locale.LocaleContextHolderBackedLocaleContext;
import at.porscheinformatik.common.spring.web.extended.locale.LocaleHandlerInterceptor;
import at.porscheinformatik.common.spring.web.extended.locale.LocaleSource;
import at.porscheinformatik.common.spring.web.extended.locale.RequestURILocaleSource;
import at.porscheinformatik.common.spring.web.extended.messagesource.DefaultMessageSourceConfig;
import at.porscheinformatik.common.spring.web.extended.messagesource.MessageSourceConfig;
import at.porscheinformatik.common.spring.web.extended.servlet.ResponseContextHandlerInterceptor;
import at.porscheinformatik.common.spring.web.extended.template.cache.DefaultStackConfig;
import at.porscheinformatik.common.spring.web.extended.template.cache.StackConfig;
import at.porscheinformatik.common.spring.web.extended.template.cache.html.HtmlStacks;
import at.porscheinformatik.common.spring.web.extended.template.cache.script.ScriptStacks;
import at.porscheinformatik.common.spring.web.extended.template.cache.style.StyleStacks;
import at.porscheinformatik.common.spring.web.extended.template.optimize.DefaultOptimizerConfig;
import at.porscheinformatik.common.spring.web.extended.template.optimize.OptimizerChain;
import at.porscheinformatik.common.spring.web.extended.template.optimize.OptimizerConfig;

@Configuration
@EnableScheduling
@EnableWebMvc
// TODO: maybe we should add a handlerinterceptor that adds no-cache headers
// for json responses
public class SpringWebExtendedConfig extends WebMvcConfigurerAdapter implements
		SchedulingConfigurer
{
	private static final Integer DEFAULT_REFRESH_INTERVALL = Integer.valueOf(5);

	@Autowired
	private Environment environment;

	private DelegatingSpringWebExtendedConfiguerer configurer = new DelegatingSpringWebExtendedConfiguerer();
	private DefaultStackConfig scriptConfig, styleConfig, htmlConfig;

	private HashMap<String, ExpressionHandler> handlers;

	@Autowired(required = false)
	public void setConfigurers(List<SpringWebExtendedConfigurer> configurers)
	{
		configurer.addConfigurers(configurers);
	}

	@Bean
	public ContextResourceScanner contextResourceScanner()
	{
		return new ContextResourceScanner();
	}

	@Bean
	public ClasspathResourceScanner classpathResourceScanner()
	{
		return new ClasspathResourceScanner();
	}

	@Bean
	public AngularResourceScanner angularResourceScanner()
	{
		return new AngularResourceScanner();
	}

	@Bean
	public WebJarResourceScanner webJarResourceScanner()
	{
		return new WebJarResourceScanner();
	}

	@Bean
	public JqueryResourceScanner jqueryResourceScanner()
	{
		return new JqueryResourceScanner();
	}

	@Bean
	public BootstrapJsResourceScanner bootstrapJsResourceScanner()
	{
		return new BootstrapJsResourceScanner();
	}

	@Bean
	public BootstrapCssResourceScanner bootstrapCssResourceScanner()
	{
		return new BootstrapCssResourceScanner();
	}

	@Bean
	public AngularUiBootstrapResourceScanner angularUiBootstrapResourceScanner()
	{
		return new AngularUiBootstrapResourceScanner();
	}

	@Bean
	public ExpressionHandlers expressionHandlers()
	{
		return new ExpressionHandlers(getExpressionHandlers());
	}

	@Bean
	public MessageSource messageSource()
	{
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

		configureMessageSource(messageSource);

		return messageSource;
	}

	@Bean
	public MessageExpressionHandler messageExpressionHandler()
	{
		return new MessageExpressionHandler();
	}

	@Bean
	public AssetExpressionHandler assetExpressionHandler()
	{
		return new AssetExpressionHandler();
	}

	@Bean
	public StyleExpressionHandler styleExpressionHandler()
	{
		return new StyleExpressionHandler(getStyleConfig(), appConfig());
	}

	@Bean
	public ScriptExpressionHandler scriptExpressionHandler()
	{
		return new ScriptExpressionHandler(getScriptConfig(), appConfig());
	}

	@Bean
	public InlineTemplateExpressionHandler inlineTemplateExpressionHandler()
	{
		return new InlineTemplateExpressionHandler();
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
	public ResourceScanners resourceScanners()
	{
		return new ResourceScanners(getScanners());
	}

	@Bean
	public StyleStacks styleStacks()
	{
		DefaultStackConfig config = getStyleConfig();

		StyleStacks styleStacks = new StyleStacks(config);
		styleStacks.setAppConfig(appConfig());
		styleStacks.setOptimizerChain(optimizerChain());
		styleStacks.setScanners(resourceScanners());
		return styleStacks;
	}

	@Bean
	public ScriptStacks scriptStacks()
	{
		DefaultStackConfig config = getScriptConfig();
		ScriptStacks scriptStacks = new ScriptStacks(config);
		scriptStacks.setAppConfig(appConfig());
		scriptStacks.setOptimizerChain(optimizerChain());
		scriptStacks.setScanners(resourceScanners());
		return scriptStacks;
	}

	@Bean
	public HtmlStacks htmlStacks()
	{
		DefaultStackConfig config = getHtmlConfig();

		HtmlStacks htmlStacks = new HtmlStacks(config);
		htmlStacks.setAppConfig(appConfig());
		htmlStacks.setOptimizerChain(optimizerChain());
		htmlStacks.setScanners(resourceScanners());
		return htmlStacks;
	}

	@Bean
	public ApplicationConfiguration appConfig()
	{
		ApplicationConfiguration config = new DefaultApplicationConfiguration();

		// Set the default optimize flag
		config.setOptimizeResources(environment
				.acceptsProfiles("optimizeresources"));

		config.addLocale("en");

		configurer.configureApplication(config);

		if (config.getVersion() == null)
		{
			byte[] bytes = new byte[15];
			new Random().nextBytes(bytes);

			try
			{
				config.setVersion(Base64.encodeBytes(bytes, Base64.URL_SAFE));
			} catch (IOException e)
			{
				throw new RuntimeException(e);
			}
		}

		return config;
	}

	@Bean
	public OptimizerChain optimizerChain()
	{
		return new OptimizerChain(getOptimizerConfig());
	}

	public DefaultStackConfig getStyleConfig()
	{
		if (styleConfig == null)
		{
			ApplicationConfiguration appConfig = appConfig();

			styleConfig = new DefaultStackConfig();
			// Default refresh intervall
			styleConfig.setRefreshIntervall(
					appConfig.isOptimizeResources()
							? -1
							: DEFAULT_REFRESH_INTERVALL);

			styleConfig.addToStack("bootstrap", "bootstrap",
					"bootstrapcss:bootstrap.css",
					"bootstrapcss:bootstrap.min.css", true);

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
			htmlConfig.setRefreshIntervall(appConfig.isOptimizeResources()
					? -1
					: DEFAULT_REFRESH_INTERVALL);

			htmlConfig.scanPath("", "templates");

			configurer.configureHtmlTemplates(htmlConfig);
		}

		return htmlConfig;
	}

	public DefaultStackConfig getScriptConfig()
	{
		if (scriptConfig == null)
		{
			ApplicationConfiguration appConfig = appConfig();

			scriptConfig = new DefaultStackConfig();
			// Default refresh intervall
			scriptConfig.setRefreshIntervall(appConfig.isOptimizeResources()
					? -1
					: DEFAULT_REFRESH_INTERVALL);

			scriptConfig.addToStack("angular", "jquery",
					"jquery:jquery.js",
					"jquery:jquery.min.js", true);
			scriptConfig.addToStack("angular", "angularjs",
					"angular:angular.js",
					"angular:angular.min.js", true);
			scriptConfig.addToStack("angular", "ngroute",
					"angular:angular-route.js",
					"angular:angular-route.min.js", true);
			scriptConfig.addToStack("angular", "bootstrap",
					"bootstrapjs:bootstrap.js", "bootstrapjs:bootstrap.min.js",
					true);
			scriptConfig.addToStack("angular", "uibootstrap",
					"angularuibootstrap:ui-bootstrap-tpls.js",
					"angularuibootstrap:ui-bootstrap-tpls.min.js", true);

			configurer.configureScripts(scriptConfig);
		}

		return scriptConfig;
	}

	private OptimizerConfig getOptimizerConfig()
	{
		OptimizerConfig config = new DefaultOptimizerConfig();

		/*
		 * We have to use the yuicompressor here because the others don't
		 * recognize @media queries
		 * https://code.google.com/p/wro4j/issues/detail?id=231
		 */
		config.addOptimizer(ResourceType.STYLE, "yuicss", new
				YUICssCompressorProcessor());
		config.addOptimizer(ResourceType.SCRIPT, "jsmin",
				new JSMinProcessor());

		configurer.configureOptimizers(config);

		return config;
	}

	private Map<String, ResourceScanner> getScanners()
	{
		Map<String, ResourceScanner> scanners = new HashMap<>();
		scanners.put("", contextResourceScanner());
		scanners.put("classpath", classpathResourceScanner());
		scanners.put("angular", angularResourceScanner());
		scanners.put("webjar", webJarResourceScanner());
		scanners.put("jquery", jqueryResourceScanner());
		scanners.put("bootstrapjs", bootstrapJsResourceScanner());
		scanners.put("bootstrapcss", bootstrapCssResourceScanner());
		scanners.put("angularuibootstrap", angularUiBootstrapResourceScanner());

		configurer.configureResourceScanners(scanners);

		return scanners;
	}

	private Map<String, ExpressionHandler> getExpressionHandlers()
	{
		if (handlers == null)
		{
			handlers = new HashMap<>();
			handlers.put("message", messageExpressionHandler());
			handlers.put("asset", assetExpressionHandler());
			handlers.put("style", styleExpressionHandler());
			handlers.put("script", scriptExpressionHandler());
			handlers.put("asset", assetExpressionHandler());
			handlers.put("inlinetemplate", inlineTemplateExpressionHandler());

			configurer.configureExpressionHandlers(handlers);
		}

		return handlers;
	}

	private void configureMessageSource(
			ReloadableResourceBundleMessageSource messageSource)
	{
		ApplicationConfiguration appConfig = appConfig();

		MessageSourceConfig config = new DefaultMessageSourceConfig();
		config.addBaseName("WEB-INF/messages/Messages");
		config.setCacheSeconds(
				appConfig.isOptimizeResources()
						? -1
						: DEFAULT_REFRESH_INTERVALL);

		configurer.configureMessageSource(config);

		messageSource.setCacheSeconds(config.getCacheSeconds() != null
				? config.getCacheSeconds()
				: -1);

		if (!CollectionUtils.isEmpty(config.getBaseNames()))
		{
			messageSource.setBasenames(config.getBaseNames().toArray(
					new String[config.getBaseNames().size()]));
		}
	}

	@Bean
	public LocaleSource requestUriLocaleSource()
	{
		return new RequestURILocaleSource();
	}

	private List<LocaleSource> getLocaleSources()
	{
		List<LocaleSource> sources = new ArrayList<>();

		sources.add(requestUriLocaleSource());
		configurer.configureLocaleSources(sources);

		return sources;
	}

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar)
	{
		StackConfig htmlConfig = getHtmlConfig();
		final HtmlStacks htmlStacks = htmlStacks();

		if (htmlConfig.getRefreshIntervall() > 0
				&& htmlStacks != null)
		{
			taskRegistrar.addFixedDelayTask(
					new Runnable() {

						@Override
						public void run()
						{
							htmlStacks.refresh();
						}
					},
					htmlConfig.getRefreshIntervall() * 1000);
		}

		StackConfig styleConfig = getStyleConfig();
		final StyleStacks styles = styleStacks();

		if (styleConfig.getRefreshIntervall() > 0
				&& styles != null)
		{

			taskRegistrar.addFixedDelayTask(
					new Runnable() {

						@Override
						public void run()
						{
							styles.refresh();
						}
					},
					styleConfig.getRefreshIntervall() * 1000);
		}

		StackConfig scriptConfig = getScriptConfig();
		final ScriptStacks scripts = scriptStacks();

		if (scriptConfig.getRefreshIntervall() > 0
				&& scripts != null)
		{

			taskRegistrar.addFixedDelayTask(
					new Runnable() {

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
		registry.addInterceptor(new ResponseContextHandlerInterceptor());

		List<LocaleSource> sources = getLocaleSources();

		if (!CollectionUtils.isEmpty(sources))
		{
			LocaleHandlerInterceptor interceptor = new LocaleHandlerInterceptor(
					sources);
			interceptor.setAvailableLocales(appConfig().getSupportedLocales());

			registry.addInterceptor(interceptor);
		}
	}
}
