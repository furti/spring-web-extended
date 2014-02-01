package at.porscheinformatik.common.springangular.resources;

import java.io.IOException;
import java.util.Map;

import org.springframework.core.io.Resource;

public interface ResourceScanner
{

	/**
	 * @param path
	 * @return Map<relativePath, Resource>
	 */
	Map<String, Resource> scanResources(String path) throws IOException;

	/**
	 * @param string
	 * @param file
	 * @param scanSubDirectories
	 * @return
	 */
	Map<String, Resource> scanResources(String path, String file,
			boolean scanSubDirectories) throws IOException;
}
