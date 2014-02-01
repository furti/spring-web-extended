package at.porscheinformatik.common.springangular.resources.cache;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContext;

import at.porscheinformatik.common.springangular.config.ApplicationConfiguration;
import at.porscheinformatik.common.springangular.resources.ResourceScanners;
import at.porscheinformatik.common.springangular.resources.StackEntry;
import at.porscheinformatik.common.springangular.resources.cache.style.StyleStacks;
import at.porscheinformatik.common.springangular.resources.optimize.OptimizerChain;
import at.porscheinformatik.common.springangular.template.parboiled.TemplateParser;

public abstract class StacksBase<T extends StackBase>
{

	private static final Logger LOG = LoggerFactory
			.getLogger(StyleStacks.class);

	private LinkedHashMap<String, T> stacks;
	private ResourceScanners scanners;
	// private ExpressionHandlers handlers;
	private LocaleContext locale;
	private OptimizerChain optimizerChain;

	private DefaultStackConfig config;

	private ApplicationConfiguration appConfig;

	private TemplateParser parser;

	public StacksBase(DefaultStackConfig config)
	{
		super();
		this.config = config;
	}

	public boolean hasStack(String stackId)
	{
		return stacks != null && stacks.containsKey(stackId);
	}

	public T get(String stackId)
	{
		return stacks != null
				? stacks.get(stackId)
				: null;
	}

	@PostConstruct
	private void setupStacks() throws IOException
	{
		stacks = new LinkedHashMap<>();
		Map<String, LinkedHashMap<String, StackEntry>> stackConfigs = config
				.getStacks();

		if (stackConfigs == null)
		{
			return;
		}

		for (Entry<String, LinkedHashMap<String, StackEntry>> entry : stackConfigs
				.entrySet())
		{
			T stack = createNewInstance();
			stack.setParser(parser);
			stack.setLocaleContex(locale);
			stack.setScanners(scanners);
			stack.setOptimizerChain(optimizerChain);
			stack.setAppConfig(appConfig);

			for (Entry<String, StackEntry> resourceEntry : entry.getValue()
					.entrySet())
			{
				stack.addResource(resourceEntry.getKey(),
						resourceEntry.getValue());
			}

			stacks.put(entry.getKey(), stack);
		}
	}

	protected abstract T createNewInstance();

	public void refresh()
	{
		if (stacks == null)
		{
			return;
		}

		for (T stack : stacks.values())
		{
			try
			{
				stack.refreshTemplates();
			} catch (IOException ex)
			{
				LOG.error("Error refreshing styles", ex);
			}
		}
	}

	public void setScanners(ResourceScanners scanners)
	{
		this.scanners = scanners;
	}

	// public void setHandlers(ExpressionHandlers handlers)
	// {
	// this.handlers = handlers;
	// }

	public void setParser(TemplateParser parser)
	{
		this.parser = parser;
	}

	public void setLocale(LocaleContext locale)
	{
		this.locale = locale;
	}

	public void setOptimizerChain(OptimizerChain optimizerChain)
	{
		this.optimizerChain = optimizerChain;
	}

	public void setAppConfig(ApplicationConfiguration appConfig)
	{
		this.appConfig = appConfig;
	}
}
