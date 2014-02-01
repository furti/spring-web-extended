package at.porscheinformatik.common.springangular.template.cache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import at.porscheinformatik.common.springangular.io.ResourceType;
import at.porscheinformatik.common.springangular.io.ResourceUtils;
import at.porscheinformatik.common.springangular.template.Template;

public abstract class StackBase extends AbstractTemplateCache
{
	private ResourceType resourceType;

	public StackBase(ResourceType resourceType)
	{
		this.resourceType = resourceType;
		setupLastRefresh();
	}

	public void addResource(String name, StackEntry entry) throws IOException
	{
		addResource(name, entry.getLocation(), false);
		addResource(name, entry.getMinifiedLocation(), true);
	}

	private void addResource(String name, String location,
			boolean optimizedResource) throws IOException
	{
		String[] pathAndFile = ResourceUtils.pathAndFile(location);

		if (pathAndFile == null)
		{
			return;
		}

		Map<String, Resource> resources = scanners.scanResources(
				pathAndFile[0], pathAndFile[1], false);

		Assert.notEmpty(resources, "No Resources for name " + name
				+ " and location " + location + " found");

		for (Entry<String, Resource> entry : resources.entrySet())
		{
			addTemplate(prepareName(name, entry.getKey()), entry.getValue(),
					resourceType, optimizedResource);
		}
	}

	/**
	 * @return
	 */
	public List<String> getNames()
	{
		// TODO: we must unlocalize the names here. Else we will publish the
		// same resource more than once
		Map<String, Template> templates = getTemplates();

		if (templates == null || templates.isEmpty())
		{
			return null;
		}

		return new ArrayList<String>(templates.keySet());
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
	 * Add the locale to the name
	 * 
	 * @param name
	 * @param key
	 * @return
	 */
	private String prepareName(String styleName, String resourceName)
	{
		String[] styleNameAndEnding = ResourceUtils.getNameAndEnding(styleName);
		String[] resourceNameAndEnding = ResourceUtils
				.getNameAndEnding(resourceName);

		String locale = ResourceUtils
				.getLocaleFromName(resourceNameAndEnding[0]);

		StringBuilder name = new StringBuilder(styleNameAndEnding[0]);

		if (locale != null)
		{
			name.append("_").append(locale);
		}

		if (styleNameAndEnding[1] != null)
		{
			name.append(styleNameAndEnding[1]);
		}

		return name.toString();
	}

}
