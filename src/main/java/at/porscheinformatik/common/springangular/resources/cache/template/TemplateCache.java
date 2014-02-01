package at.porscheinformatik.common.springangular.resources.cache.template;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import at.porscheinformatik.common.springangular.resources.ResourceType;
import at.porscheinformatik.common.springangular.resources.cache.AbstractTemplateCache;
import at.porscheinformatik.common.springangular.resources.cache.CacheEntryConfig;
import at.porscheinformatik.common.springangular.resources.cache.CacheUtils;

/**
 * @author Daniel Furtlehner
 * 
 */
public class TemplateCache extends AbstractTemplateCache
{
	private Map<String, CacheEntryConfig> config;

	public TemplateCache(Map<String, CacheEntryConfig> config)
	{
		Assert.notEmpty(config,
				"At least one templatecache configuration must be supplied");
		this.config = config;
	}

	@PostConstruct
	public void setupTemplates() throws IOException
	{
		setupLastRefresh();

		for (Entry<String, CacheEntryConfig> entry : config.entrySet())
		{
			CacheEntryConfig config = entry.getValue();

			Assert.hasText(config.getLocationPrefix(),
					"Locationprefix must not be empty");

			Map<String, Resource> configTemplates = getTemplateResourcesForLocation(
					entry.getKey(), config.getLocationPrefix());

			if (configTemplates != null)
			{
				for (Entry<String, Resource> configEntry : configTemplates
						.entrySet())
				{
					addTemplate(
							configEntry.getKey().toLowerCase()
									.replace("\\", "/"),
							configEntry.getValue(), ResourceType.TEMPLATE,
							false);
				}
			}
		}
	}

	private Map<String, Resource> getTemplateResourcesForLocation(
			String configName,
			String location)
	{
		try
		{
			Map<String, Resource> templateResources = scanners
					.scanResources(location);

			Map<String, Resource> temp = new HashMap<>();

			if (templateResources != null)
			{
				for (Entry<String, Resource> templateEntry : templateResources
						.entrySet())
				{
					String path = CacheUtils.buildPath(configName,
							templateEntry.getKey());

					temp.put(path, templateEntry.getValue());
				}
			}

			return temp;
		} catch (IOException ex)
		{
			throw new RuntimeException("Error reading templates", ex);
		}
	}
}
