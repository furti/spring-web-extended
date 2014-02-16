package at.porscheinformatik.common.springangular.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class ClasspathResourceScanner implements ResourceScanner
{

	private ResourcePatternResolver resourcePatternResolver =
			new PathMatchingResourcePatternResolver();

	@Override
	public Map<String, Resource> scanResources(String path) throws IOException
	{
		String packageSearchPath = buildScanPath(path, null, true);

		Resource[] resources = resourcePatternResolver
				.getResources(packageSearchPath);

		if (resources == null || resources.length == 0)
		{
			return null;
		}

		return createResources(path, resources);
	}

	@Override
	public Map<String, Resource> scanResources(String path, String file,
			boolean scanSubDirectories) throws IOException
	{
		String packageSearchPath = buildScanPath(path, file,
				scanSubDirectories);

		Resource[] resources = filterResources(resourcePatternResolver
				.getResources(packageSearchPath), file);

		if (resources == null || resources.length == 0)
		{
			return null;
		}

		return createResources(path, resources);
	}

	/**
	 * Remove all resources that are not a localized form of the base resource
	 * 
	 * @param resources
	 * @param file
	 * @return
	 */
	private Resource[] filterResources(Resource[] resources, String file)
	{
		if (resources == null || file == null)
		{
			return resources;
		}

		List<Resource> filtered = new ArrayList<>();

		for (Resource resource : resources)
		{
			String filename = resource.getFilename();
			String[] nameAndEnding = ResourceUtils.getNameAndEnding(file);

			int baseLength = nameAndEnding[0].length();

			if (nameAndEnding[1] != null)
			{
				baseLength += nameAndEnding[1].length();
			}

			// If the character followed by the name is a _ it separates the
			// file from the locale
			if (filename.length() <= baseLength
					|| filename.charAt(nameAndEnding[0].length()) == '_')
			{
				filtered.add(resource);
			}
		}

		return filtered.toArray(new Resource[filtered.size()]);
	}

	private Map<String, Resource> createResources(String rootPath,
			Resource[] resourceArray) throws IOException
	{
		Map<String, Resource> resources = new HashMap<>();

		for (Resource resource : resourceArray)
		{
			String path = resource.getURL().toString();

			resources.put(constructRelativePath(path, rootPath), resource);
		}

		return resources;
	}

	String buildScanPath(String path, String file,
			boolean scanSubDirectories)
	{
		StringBuilder packageSearchPath = new StringBuilder(
				ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX);
		packageSearchPath.append(path);

		if (scanSubDirectories)
		{
			packageSearchPath.append("/**/");
		}
		else if (!path.endsWith("/"))
		{
			packageSearchPath.append("/");
		}

		if (file != null)
		{
			String[] nameAndEnding = ResourceUtils.getNameAndEnding(file);

			packageSearchPath.append(nameAndEnding[0]).append("*");

			if (nameAndEnding[1] != null)
			{
				packageSearchPath.append(nameAndEnding[1]);
			}
			else
			{
				packageSearchPath.append(".*");
			}
		}
		else
		{
			packageSearchPath.append("*.*");
		}

		return packageSearchPath.toString();
	}

	private String constructRelativePath(String path, String rootPath)
	{
		Pattern p = Pattern.compile("^.*" + rootPath + "/(.*)$");

		Matcher m = p.matcher(path);

		if (!m.matches() || m.groupCount() < 1)
		{
			return path;
		}

		return m.group(1);
	}
}
