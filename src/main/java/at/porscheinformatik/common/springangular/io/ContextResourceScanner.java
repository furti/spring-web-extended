package at.porscheinformatik.common.springangular.io;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.ServletContextAware;

/**
 * Scans the webapp directory for resources.
 * 
 * @author Daniel Furtlehner
 * 
 */
public class ContextResourceScanner implements ServletContextAware,
		ResourceScanner
{

	private ServletContext context;

	public void setServletContext(ServletContext context)
	{
		this.context = context;
	}

	@Override
	public Map<String, Resource> scanResources(String path) throws IOException
	{
		Path rootPath = buildRootPath(path);

		List<Path> templateFiles = findTemplates(
				Files.newDirectoryStream(rootPath), null, true);

		return createResources(rootPath, templateFiles);
	}

	@Override
	public Map<String, Resource> scanResources(String path, String file,
			boolean scanSubDirectories) throws IOException
	{
		Path rootPath = buildRootPath(path);
		final String[] nameAndEnding = ResourceUtils
				.getNameAndEnding(file);

		List<Path> templateFiles = findTemplates(
				Files.newDirectoryStream(rootPath),
				new FileMatcher() {

					@Override
					public boolean matches(Path path)
					{
						String filename = path.getFileName().toString();
						int baselength = nameAndEnding[0].length();

						if (!filename.startsWith(nameAndEnding[0]))
						{
							return false;
						}

						if (nameAndEnding[1] != null)
						{
							baselength += nameAndEnding[1].length();

							if (!filename.endsWith(nameAndEnding[1]))
							{
								return false;
							}
						}

						/*
						 * If the file has more characters than the basename the
						 * character following the filename must be a _ to be an
						 * locale. Else we might have a minimized version of the
						 * same resource. So ignore it
						 */
						if (filename.length() > baselength)
						{
							return filename.charAt(nameAndEnding[1].length()) == '_';
						}

						return true;
					}

				}, scanSubDirectories);

		return createResources(rootPath, templateFiles);
	}

	private Path buildRootPath(String path) throws IOException
	{
		Assert.hasText(path, "Path must not be empty");

		String contextPath = context.getRealPath("/");

		Assert.notNull(contextPath,
				"Could not get contextPath from ServletContext");

		Path rootPath = Paths.get(contextPath, path);

		if (!Files.isDirectory(rootPath))
		{
			throw new IOException("Directory " + rootPath.toAbsolutePath()
					+ " does not exist or is no directory");
		}

		return rootPath;
	}

	private List<Path> findTemplates(DirectoryStream<Path> directory,
			FileMatcher matcher, boolean scanSubDirectories)
			throws IOException
	{
		List<Path> files = new ArrayList<>();

		try
		{
			for (Path p : directory)
			{
				if (Files.isDirectory(p) && scanSubDirectories)
				{
					files.addAll(findTemplates(
							Files.newDirectoryStream(p),
							matcher,
							scanSubDirectories));
				}
				else
				{
					if (matcher == null || matcher.matches(p))
					{
						files.add(p);
					}
				}
			}
		} finally
		{
			directory.close();
		}

		return files;
	}

	private Map<String, Resource> createResources(Path rootPath,
			List<Path> templateFiles)
	{
		if (CollectionUtils.isEmpty(templateFiles))
		{
			return null;
		}

		Map<String, Resource> resources = new HashMap<String, Resource>();

		for (Path templateFile : templateFiles)
		{
			Path relative = rootPath.relativize(templateFile);

			resources.put(relative.toString().replace("\\", "/"),
					new FileSystemResource(
							templateFile.toFile()));
		}

		return resources;
	}

	private static interface FileMatcher
	{

		boolean matches(Path path);
	}
}
