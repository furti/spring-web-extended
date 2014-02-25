package at.porscheinformatik.common.spring.web.extended.template.cache;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.context.i18n.LocaleContext;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import at.porscheinformatik.common.spring.web.extended.config.ApplicationConfiguration;
import at.porscheinformatik.common.spring.web.extended.expression.ExpressionHandlers;
import at.porscheinformatik.common.spring.web.extended.io.ResourceScanners;
import at.porscheinformatik.common.spring.web.extended.io.ResourceType;
import at.porscheinformatik.common.spring.web.extended.io.ResourceUtils;
import at.porscheinformatik.common.spring.web.extended.template.Template;
import at.porscheinformatik.common.spring.web.extended.template.optimize.OptimizerChain;
import at.porscheinformatik.common.spring.web.extended.template.parboiled.ParboiledTemplate;

public abstract class AbstractTemplateCache
{

	private LinkedHashMap<String, Template> templates = new LinkedHashMap<>();
	private LinkedHashMap<String, Template> optimizedTemplates = new LinkedHashMap<>();
	protected ResourceScanners scanners;
	private LocaleContext locale;
	private ApplicationConfiguration appConfig;
	private OptimizerChain optimizerChain;
	private Date lastRefresh;
	private ExpressionHandlers expressionHandlers;

	protected void setupLastRefresh()
	{
		lastRefresh = new Date();
	}

	public boolean hasTemplate(String templateName)
	{
		List<String> localizedTemplates = ResourceUtils.localizedResources(
				templateName, locale.getLocale());

		Assert.notNull(localizedTemplates,
				"Could not localize templates for locale " + locale.getLocale());

		for (String name : localizedTemplates)
		{
			if (templates.containsKey(name))
			{
				return true;
			}
		}

		return false;
	}

	public String renderTemplate(String templateName)
	{
		List<String> localizedTemplates = ResourceUtils.localizedResources(
				templateName, locale.getLocale());

		Assert.notNull(localizedTemplates,
				"Could not localize templates for locale " + locale.getLocale());

		String result = null;

		if (shouldOptimize())
		{
			result = renderTemplateFromMap(localizedTemplates,
					optimizedTemplates);
		}

		// IF no optimized template was rendered, try to render a not optimized
		// one
		if (result == null)
		{
			result = renderTemplateFromMap(localizedTemplates, templates);
		}

		return result;
	}

	public void refreshTemplates() throws IOException
	{
		if (templates != null && !templates.isEmpty())
		{
			for (Template template : templates.values())
			{
				if (template.isChanged(lastRefresh))
				{
					template.refresh();
				}
			}
		}

		if (optimizedTemplates != null && !optimizedTemplates.isEmpty())
		{
			for (Template template : optimizedTemplates.values())
			{
				if (template.isChanged(lastRefresh))
				{
					template.refresh();
				}
			}
		}

		lastRefresh = new Date();
	}

	protected Map<String, Template> getTemplates()
	{
		return templates;
	}

	protected void addTemplate(String name, Resource resource,
			ResourceType type, boolean optimizedResource)
			throws IOException
	{
		ParboiledTemplate template = new ParboiledTemplate(resource,
				expressionHandlers, type);

		if (optimizedResource)
		{
			Assert.isNull(
					optimizedTemplates.put(name, template),
					"Template "
							+ name
							+ " was added twice to the cache for optimized Templates");
		}
		else
		{
			if (shouldOptimize())
			{
				template.setOptimizerChain(optimizerChain);
			}

			Assert.isNull(
					templates.put(name, template),
					"Template " + name + " was added twice to the cache");
		}
	}

	public void removeTemplate(String templateName)
	{
		if (templateName == null)
		{
			return;
		}

		Iterator<Entry<String, Template>> iterator = templates.entrySet()
				.iterator();
		while (iterator.hasNext())
		{
			if (iterator.next().getKey().startsWith(templateName))
			{
				iterator.remove();
			}
		}
	}

	public void setLocaleContex(LocaleContext locale)
	{
		this.locale = locale;
	}

	public void setScanners(ResourceScanners scanners)
	{
		this.scanners = scanners;
	}

	public void setAppConfig(ApplicationConfiguration appConfig)
	{
		this.appConfig = appConfig;
	}

	public void setOptimizerChain(OptimizerChain optimizerChain)
	{
		this.optimizerChain = optimizerChain;
	}

	private boolean shouldOptimize()
	{
		return appConfig != null && appConfig.isOptimizeResources();
	}

	private String renderTemplateFromMap(List<String> localizedTemplates,
			LinkedHashMap<String, Template> templatesToUse)
	{
		for (String name : localizedTemplates)
		{
			Template template = templatesToUse.get(name);

			if (template != null)
			{
				try
				{
					return template.render();
				} catch (IOException ex)
				{
					throw new RuntimeException("Error rendering template "
							+ name, ex);
				}
			}
		}
		return null;
	}

	public void setExpressionHandlers(ExpressionHandlers expressionHandlers)
	{
		this.expressionHandlers = expressionHandlers;
	}
}
