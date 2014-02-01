package at.porscheinformatik.common.springangular.resources;

import java.io.IOException;
import java.util.Map;

import org.springframework.core.io.Resource;

public class AngularResourceScanner extends ClasspathResourceScanner
{
	private static final String BASE_PATH = "at/porscheinformatik/common/springangular/asset/";

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
			return BASE_PATH;
		}

		return BASE_PATH + path;
	}
}