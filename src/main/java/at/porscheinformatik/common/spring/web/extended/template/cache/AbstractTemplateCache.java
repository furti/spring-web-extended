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
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import at.porscheinformatik.common.spring.web.extended.http.LinkCreator;
import at.porscheinformatik.common.spring.web.extended.io.ResourceScanners;
import at.porscheinformatik.common.spring.web.extended.io.ResourceType;
import at.porscheinformatik.common.spring.web.extended.io.ResourceUtils;
import at.porscheinformatik.common.spring.web.extended.template.CssAssetUrlProcessor;
import at.porscheinformatik.common.spring.web.extended.template.DefaultTemplateRenderContext;
import at.porscheinformatik.common.spring.web.extended.template.StringTemplate;
import at.porscheinformatik.common.spring.web.extended.template.Template;
import at.porscheinformatik.common.spring.web.extended.template.TemplateFactory;
import at.porscheinformatik.common.spring.web.extended.template.TemplateRenderContext;
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
	private CssAssetUrlProcessor urlRewritingProcessor;
	protected ResourceScanners scanners;
	private ApplicationConfiguration appConfig;
	private OptimizerChain optimizerChain;
	private Date lastRefresh;

	private Map<RenderCacheKey, String> renderCache = new HashMap<RenderCacheKey, String>();

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
			DefaultTemplateRenderContext context = new DefaultTemplateRenderContext(
					locale,
					template.getType());

			// If the template was rendered and cached before we use the already
			// rendered one
			String fromCache = checkCache(context, template);
			if (fromCache != null)
			{
				return fromCache;
			}

			TemplateRenderContextHolder
					.setCurrentContext(context);

			String result = template.render();

			if (template.getType() == ResourceType.STYLE)
			{
				result = prepareRelativeUrls(result, template);
			}

			TemplateRenderContextHolder.removeCurrentContext();

			if (optimizerChain != null
					&& shouldOptimize()
					&& !template.isAlreadyOptimized())
			{
				result = optimizerChain.optimize(template.getType(),
						template.getName(), result);
			}

			if (shouldOptimize())
			{
				addToCache(context, template, result);
			}

			return result;
		} catch (IOException ex)
		{
			throw new RuntimeException("Error rendering template "
					+ template.getName(), ex);
		}
	}

	private String prepareRelativeUrls(String result, Template template)
			throws IOException
	{
		if (result == null)
		{
			return null;
		}

		ro.isdc.wro.model.resource.Resource resource = new ro.isdc.wro.model.resource.Resource();
		resource.setUri(template.getLocation());

		StringWriter writer = new StringWriter();
		urlRewritingProcessor.process(resource, new StringReader(result), writer);

		return writer.toString();

	}

	private void addToCache(DefaultTemplateRenderContext context,
			Template template, String content)
	{
		RenderCacheKey key = new RenderCacheKey(context, template.getName());
		renderCache.put(key, content);
	}

	private String checkCache(DefaultTemplateRenderContext context,
			Template template)
	{
		RenderCacheKey key = new RenderCacheKey(context, template.getName());
		return renderCache.get(key);
	}

	public String renderAll()
	{
		List<String> names = getNames();

		if (names == null)
		{
			return null;
		}

		StringBuilder content = new StringBuilder();

		for (String name : names)
		{
			content.append(renderTemplate(name)).append("\n");
		}

		return content.toString();
	}

	/**
	 * @return
	 */
	public List<String> getNames()
	{
		Map<String, Template> templates = getTemplates();

		if (templates == null || templates.isEmpty())
		{
			return new ArrayList<>();
		}

		List<String> names = new ArrayList<>();

		for (String name : templates.keySet())
		{
			String unlocalized = ResourceUtils.unlocalize(name);

			if (!names.contains(unlocalized))
			{
				names.add(unlocalized);
			}
		}

		return names;
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

	protected void addTemplate(String name, String location, Resource resource,
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
					optimizedResource, resource, location);
		}
		else
		{
			template = templateFactory.createTemplate(resource,
					templateName, location, type, optimizedResource);
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

	public void setLinkCreator(LinkCreator linkCreator)
	{
		urlRewritingProcessor = new CssAssetUrlProcessor(linkCreator);
	}

	/**
	 * @author Daniel Furtlehner
	 * 
	 */
	private static class RenderCacheKey
	{
		private TemplateRenderContext context;
		private String template;

		public RenderCacheKey(TemplateRenderContext context, String template)
		{
			super();
			this.context = context;
			this.template = template;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((context == null) ? 0 : context.hashCode());
			result = prime * result
					+ ((template == null) ? 0 : template.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
			{
				return true;
			}

			if (obj == null)
			{
				return false;
			}

			if (getClass() != obj.getClass())
			{
				return false;
			}

			RenderCacheKey other = (RenderCacheKey) obj;
			if (context == null)
			{
				if (other.context != null)
				{
					return false;
				}
			} else if (!context.equals(other.context))
			{
				return false;
			}

			if (template == null)
			{
				if (other.template != null)
				{
					return false;
				}
			} else if (!template.equals(other.template))
			{
				return false;
			}

			return true;
		}
	}
}
