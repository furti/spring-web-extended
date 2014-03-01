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
package at.porscheinformatik.common.spring.web.extended.template.cache;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import at.porscheinformatik.common.spring.web.extended.config.ApplicationConfiguration;
import at.porscheinformatik.common.spring.web.extended.io.ResourceScanners;
import at.porscheinformatik.common.spring.web.extended.io.ResourceType;
import at.porscheinformatik.common.spring.web.extended.io.ResourceUtils;
import at.porscheinformatik.common.spring.web.extended.template.DefaultTemplateRenderContext;
import at.porscheinformatik.common.spring.web.extended.template.StringTemplate;
import at.porscheinformatik.common.spring.web.extended.template.Template;
import at.porscheinformatik.common.spring.web.extended.template.TemplateFactory;
import at.porscheinformatik.common.spring.web.extended.template.TemplateRenderContextHolder;
import at.porscheinformatik.common.spring.web.extended.template.optimize.OptimizerChain;

/**
 * @author Daniel Furtlehner
 * 
 */
public abstract class AbstractTemplateCache
{

	private TemplateFactory templateFactory;
	private String cacheName;

	private LinkedHashMap<String, Template> templates = new LinkedHashMap<>();
	private LinkedHashMap<String, Template> optimizedTemplates = new LinkedHashMap<>();
	protected ResourceScanners scanners;
	private ApplicationConfiguration appConfig;
	private OptimizerChain optimizerChain;
	private Date lastRefresh;

	public AbstractTemplateCache(String cacheName)
	{
		this.cacheName = cacheName;
	}

	protected void setupLastRefresh()
	{
		lastRefresh = new Date();
	}

	public boolean hasTemplate(String templateName)
	{
		Locale locale = LocaleContextHolder.getLocale();

		List<String> localizedTemplates = ResourceUtils.localizedResources(
				templateName, locale);

		Assert.notNull(localizedTemplates,
				"Could not localize templates for locale " + locale);

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
		Locale locale = LocaleContextHolder.getLocale();

		List<String> localizedTemplates = ResourceUtils.localizedResources(
				templateName, locale);

		Assert.notNull(localizedTemplates,
				"Could not localize templates for locale " + locale);

		Template template = null;

		if (shouldOptimize())
		{
			template = getTemplateFromMap(localizedTemplates,
					optimizedTemplates);
		}

		// IF no optimized template was found search for a not optimized one
		if (template == null)
		{
			template = getTemplateFromMap(localizedTemplates, templates);
		}

		if (template == null)
		{
			return null;
		}

		try
		{
			// TODO: hier noch eine factory einführen
			// Set the rendercontext before rendering the template
			TemplateRenderContextHolder
					.setCurrentContext(new DefaultTemplateRenderContext(locale,
							template.getType()));

			String result = template.render();

			TemplateRenderContextHolder.removeCurrentContext();

			if (optimizerChain != null
					&& shouldOptimize()
					&& !template.isAlreadyOptimized())
			{
				result = optimizerChain.optimize(template.getType(),
						template.getName(), result);
			}

			return result;
		} catch (IOException ex)
		{
			throw new RuntimeException("Error rendering template "
					+ template.getName(), ex);
		}
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
			ResourceType type, boolean optimizedResource, boolean skipProcessing)
			throws IOException
	{
		String templateName = cacheName + ":" + name;

		Template template = null;

		// If the template should not be processed by an template engine we use
		// the StringTemplate
		if (skipProcessing)
		{
			template = new StringTemplate(type, templateName,
					optimizedResource, resource);
		}
		else
		{
			template = templateFactory.createTemplate(resource,
					templateName, type,
					optimizedResource);
		}

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

	private boolean shouldOptimize()
	{
		return appConfig != null && appConfig.isOptimizeResources();
	}

	private Template getTemplateFromMap(List<String> localizedTemplates,
			LinkedHashMap<String, Template> templatesToUse)
	{
		for (String name : localizedTemplates)
		{
			Template template = templatesToUse.get(name);

			if (template != null)
			{
				return template;
			}
		}

		return null;
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

	public void setTemplateFactory(TemplateFactory templateFactory)
	{
		this.templateFactory = templateFactory;
	}
}
