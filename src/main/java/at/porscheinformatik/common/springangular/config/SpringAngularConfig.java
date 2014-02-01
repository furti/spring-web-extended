package at.porscheinformatik.common.springangular.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.parboiled.Parboiled;
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

import ro.isdc.wro.extensions.processor.js.GoogleClosureCompressorProcessor;
import ro.isdc.wro.model.resource.processor.impl.css.CssMinProcessor;
import at.porscheinformatik.common.springangular.expression.AssetExpressionHandler;
import at.porscheinformatik.common.springangular.expression.ExpressionHandler;
import at.porscheinformatik.common.springangular.expression.InlineTemplateExpressionHandler;
import at.porscheinformatik.common.springangular.expression.MessageExpressionHandler;
import at.porscheinformatik.common.springangular.expression.ScriptExpressionHandler;
import at.porscheinformatik.common.springangular.expression.ScriptExpressionHandlers;
import at.porscheinformatik.common.springangular.expression.StyleExpressionHandler;
import at.porscheinformatik.common.springangular.expression.StyleExpressionHandlers;
import at.porscheinformatik.common.springangular.expression.TemplateExpressionHandlers;
import at.porscheinformatik.common.springangular.locale.LocaleContextHolderBackedLocaleContext;
import at.porscheinformatik.common.springangular.messagesource.DefaultMessageSourceConfig;
import at.porscheinformatik.common.springangular.messagesource.MessageSourceConfig;
import at.porscheinformatik.common.springangular.resources.AngularResourceScanner;
import at.porscheinformatik.common.springangular.resources.ClasspathResourceScanner;
import at.porscheinformatik.common.springangular.resources.ContextResourceScanner;
import at.porscheinformatik.common.springangular.resources.LocalizedResourceLoader;
import at.porscheinformatik.common.springangular.resources.LocalizedResourceLoaderImpl;
import at.porscheinformatik.common.springangular.resources.ResourceScanner;
import at.porscheinformatik.common.springangular.resources.ResourceScanners;
import at.porscheinformatik.common.springangular.resources.ResourceType;
import at.porscheinformatik.common.springangular.resources.cache.CacheConfig;
import at.porscheinformatik.common.springangular.resources.cache.CacheRefreshTask;
import at.porscheinformatik.common.springangular.resources.cache.DefaultCacheConfig;
import at.porscheinformatik.common.springangular.resources.cache.DefaultCacheEntryConfig;
import at.porscheinformatik.common.springangular.resources.cache.DefaultStackConfig;
import at.porscheinformatik.common.springangular.resources.cache.StackConfig;
import at.porscheinformatik.common.springangular.resources.cache.script.ScriptStacks;
import at.porscheinformatik.common.springangular.resources.cache.style.StyleStacks;
import at.porscheinformatik.common.springangular.resources.cache.template.TemplateCache;
import at.porscheinformatik.common.springangular.resources.optimize.DefaultOptimizerConfig;
import at.porscheinformatik.common.springangular.resources.optimize.OptimizerChain;
import at.porscheinformatik.common.springangular.resources.optimize.OptimizerConfig;
import at.porscheinformatik.common.springangular.template.parboiled.TemplateParser;

@Configuration
@EnableScheduling
public class SpringAngularConfig implements SchedulingConfigurer
{
	private static final Integer DEFAULT_REFRESH_INTERVALL = Integer.valueOf(5);

	@Autowired
	private Environment environment;

	// TODO: maybe we should add a handlerinterceptor that adds no-cache headers
	// for json responses
	private DelegatingSpringAngularConfiguerer configurer = new DelegatingSpringAngularConfiguerer();
	private CacheConfig templateCacheConfig;
	private DefaultStackConfig scriptConfig, styleConfig;

	@Autowired(required = false)
	public void setConfigurers(List<SpringAngularConfigurer> configurers)
	{
		configurer.addConfigurers(configurers);
	}

	@Bean
	public TemplateCache templateCache()
	{
		CacheConfig config = getTemplateCacheConfig();
		TemplateCache templateCache = new TemplateCache(
				config.getTemplateConfig());
		templateCache.setAppConfig(appConfig());
		// templateCache.setHandlers(templateExpressionHandlers());
		templateCache.setParser(templateParser(config));
		templateCache.setLocaleContex(localeContext());
		templateCache.setOptimizerChain(optimizerChain());
		templateCache.setScanners(resourceScanners());
		return templateCache;
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

	public TemplateParser scriptParser(StackConfig config)
	{
		return Parboiled.createParser(TemplateParser.class,
				new ScriptExpressionHandlers(getScriptExpressionHandlers()));
	}

	public TemplateParser styleParser(StackConfig config)
	{
		return Parboiled.createParser(TemplateParser.class,
				new StyleExpressionHandlers(getStyleExpressionHandlers()));
	}

	public TemplateParser templateParser(CacheConfig config)
	{
		Map<String, ExpressionHandler> expressionHandlers = getTemplateExpressionHandlers();

		if (config.getExpressionSuffix() != null
				&& config.getExpressionDelimiter() != null
				&& config.getExpressionPrefix() != null)
		{
			return Parboiled.createParser(TemplateParser.class,
					expressionHandlers, config.getExpressionPrefix(),
					config.getExpressionSuffix(),
					config.getExpressionDelimiter());
		}
		else
		{
			return Parboiled.createParser(TemplateParser.class,
					new TemplateExpressionHandlers(expressionHandlers));
		}
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
		// styleStacks.setHandlers(styleExpressionHandlers());
		styleStacks.setParser(styleParser(config));
		styleStacks.setLocale(localeContext());
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
		// scriptStacks.setHandlers(scriptExpressionHandlers());
		scriptStacks.setParser(scriptParser(config));
		scriptStacks.setLocale(localeContext());
		scriptStacks.setOptimizerChain(optimizerChain());
		scriptStacks.setScanners(resourceScanners());
		return scriptStacks;
	}

	@Bean
	public ApplicationConfiguration appConfig()
	{
		ApplicationConfiguration config = new DefaultApplicationConfiguration();

		// Set the default optimize flag
		config.setOptimizeResources(environment
				.acceptsProfiles("optimizeangular"));

		configurer.configureApplication(config);

		if (config.getVersion() == null)
		{
			// TODO: set the default application version here
		}

		return config;
	}

	@Bean
	public OptimizerChain optimizerChain()
	{
		return new OptimizerChain(getOptimizerConfig());
	}

	// @Bean
	public DefaultStackConfig getStyleConfig()
	{
		if (styleConfig == null)
		{
			styleConfig = new DefaultStackConfig();
			// Default refresh intervall
			styleConfig.setRefreshIntervall(DEFAULT_REFRESH_INTERVALL);

			// styleConfig.addToStack("angular", "bootstrap",
			// "angular:style/bootstrap.css",
			// "angular:style/bootstrap.min.js");

			configurer.configureStyles(styleConfig);
		}

		return styleConfig;
	}

	public DefaultStackConfig getScriptConfig()
	{
		if (scriptConfig == null)
		{
			scriptConfig = new DefaultStackConfig();
			// Default refresh intervall
			scriptConfig.setRefreshIntervall(DEFAULT_REFRESH_INTERVALL);

			scriptConfig.addToStack("angular", "jquery",
					"angular:script/jquery.js", "angular:script/jquery.min.js");
			scriptConfig.addToStack("angular", "angularjs",
					"angular:script/angular.js",
					"angular:script/angular.min.js");
			scriptConfig.addToStack("angular", "ngroute",
					"angular:script/angular-route.js",
					"angular:script/angular-route.min.js");
			scriptConfig.addToStack("angular", "uibootstrap",
					"angular:script/ui-bootstrap.js",
					"angular:script/ui-bootstrap.min.js");

			configurer.configureScripts(scriptConfig);
		}

		return scriptConfig;
	}

	private OptimizerConfig getOptimizerConfig()
	{
		OptimizerConfig config = new DefaultOptimizerConfig();

		// config.addOptimizer(ResourceType.STYLE, "datauri",
		// new CssDataUriPreProcessor());
		config.addOptimizer(ResourceType.STYLE, "cssmin", new CssMinProcessor());
		config.addOptimizer(ResourceType.SCRIPT, "googleclosure",
				new GoogleClosureCompressorProcessor());

		configurer.configureOptimizers(config);

		return config;
	}

	private Map<String, ResourceScanner> getScanners()
	{
		Map<String, ResourceScanner> scanners = new HashMap<>();
		scanners.put("", contextResourceScanner());
		scanners.put("classpath", classpathResourceScanner());
		scanners.put("angular", angularResourceScanner());

		// TODO: call configurer.configureScanners(config)

		return scanners;
	}

	private CacheConfig getTemplateCacheConfig()
	{
		if (templateCacheConfig == null)
		{
			templateCacheConfig = new DefaultCacheConfig();
			templateCacheConfig.setRefreshIntervall(DEFAULT_REFRESH_INTERVALL);

			templateCacheConfig.addTemplateConfig("",
					new DefaultCacheEntryConfig(
							"/templates"));

			configurer.addTemplateCacheConfig(templateCacheConfig);
		}

		return templateCacheConfig;
	}

	private Map<String, ExpressionHandler> getTemplateExpressionHandlers()
	{
		Map<String, ExpressionHandler> handlers = new HashMap<>();
		handlers.put("message", messageExpressionHandler());
		handlers.put("asset", assetExpressionHandler());
		handlers.put("style", styleExpressionHandler());
		handlers.put("script", scriptExpressionHandler());

		return handlers;
	}

	private Map<String, ExpressionHandler> getScriptExpressionHandlers()
	{
		Map<String, ExpressionHandler> handlers = new HashMap<>();
		handlers.put("message", messageExpressionHandler());
		handlers.put("asset", assetExpressionHandler());
		handlers.put("inlinetemplate", inlineTemplateExpressionHandler());

		return handlers;
	}

	private Map<String, ExpressionHandler> getStyleExpressionHandlers()
	{
		Map<String, ExpressionHandler> handlers = new HashMap<>();
		handlers.put("message", messageExpressionHandler());
		handlers.put("asset", assetExpressionHandler());

		return handlers;
	}

	private void configureMessageSource(
			ReloadableResourceBundleMessageSource messageSource)
	{
		MessageSourceConfig config = new DefaultMessageSourceConfig();
		config.addBaseName("WEB-INF/messages/Messages");
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

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar)
	{
		CacheConfig config = getTemplateCacheConfig();

		if (config.getRefreshIntervall() > 0)
		{
			taskRegistrar.addFixedDelayTask(
					new CacheRefreshTask(templateCache()),
					config.getRefreshIntervall() * 1000);
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
}
