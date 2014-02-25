package at.porscheinformatik.common.spring.web.extended.io;

import java.io.IOException;
import java.util.Map;

import org.springframework.core.io.Resource;

public abstract class ClasspathShortcutResourceScanner extends
		ClasspathResourceScanner
{

	private String basePath;

	public ClasspathShortcutResourceScanner(String basePath)
	{
		this.basePath = basePath;
	}

	@Override
	public Map<String, Resource> scanResources(String path) throws IOException
	{
		return super.scanResources(preparePath(path));
	}

	@Override
	public Map<String, Resource> scanResources(String path, String file,
			boolean scanSubDirectories) throws IOException
	{
		return super.scanResources(preparePath(path), file, scanSubDirectories);
	}

	private String preparePath(String path)
	{
		if (path == null)
		{
			return basePath;
		}

		return basePath + path;
	}
}
