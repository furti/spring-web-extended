package at.porscheinformatik.common.spring.web.extended.io;

import java.io.IOException;
import java.util.Map;

import org.springframework.core.io.Resource;

public interface ResourceScanner
{

	/**
	 * Scans all resource
	 * 
	 * @param path
	 * @return Map<relativePath, Resource>
	 */
	Map<String, Resource> scanResources(String path) throws IOException;

	/**
	 * @param string
	 * @param file
	 * @param scanSubDirectories
	 * @return Map<relativePath, Resource>
	 */
	Map<String, Resource> scanResources(String path, String file,
			boolean scanSubDirectories) throws IOException;
}
