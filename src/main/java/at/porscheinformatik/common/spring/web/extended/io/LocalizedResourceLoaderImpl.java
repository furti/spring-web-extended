package at.porscheinformatik.common.spring.web.extended.io;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class LocalizedResourceLoaderImpl implements LocalizedResourceLoader
{

	private ResourceLoader resourceLoader;

	@Override
	public Resource getResource(String resource, Locale locale)
	{
		List<String> resourceNames = ResourceUtils.localizedResources(resource,
				locale);

		if (resourceNames == null)
		{
			return null;
		}

		for (String resourceLocation : resourceNames)
		{
			Resource r = resourceLoader.getResource(resourceLocation);

			if (r != null && r.exists())
			{
				return r;
			}
		}

		return null;
	}

	@Autowired
	public void setResourceLoader(ResourceLoader resourceLoader)
	{
		this.resourceLoader = resourceLoader;
	}

}
