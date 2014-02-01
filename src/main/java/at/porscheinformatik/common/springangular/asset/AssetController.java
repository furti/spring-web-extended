package at.porscheinformatik.common.springangular.asset;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.ServletContextAware;

import at.porscheinformatik.common.springangular.io.LocalizedResourceLoader;
import at.porscheinformatik.common.springangular.util.RequestUtils;
import at.porscheinformatik.common.springangular.util.ResourceNotFoundException;

/**
 * Controller that sends static resources to the client.
 * 
 * @author Daniel Furtlehner
 * 
 */
@Controller
public class AssetController implements ServletContextAware
{

	private static final Pattern PATH_PATTERN = Pattern
			.compile("^.*asset/(.*)");

	private LocalizedResourceLoader resourceLoader;
	private LocaleContext locale;
	private ServletContext servletContext;

	@RequestMapping(value = "/**/asset/**", method = RequestMethod.GET)
	public void handleAsset(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String path = RequestUtils.getPathFromRegex(request, PATH_PATTERN);
		String resourcePath = buildResourceFromPath(path);

		// TODO: we should improve loading of resources
		Resource resource = resourceLoader.getResource(resourcePath,
				locale.getLocale());

		if (resource == null || !resource.exists())
		{
			throw new ResourceNotFoundException();
		}

		String mimeType = servletContext.getMimeType(path);

		if (mimeType != null)
		{
			response.setContentType(mimeType);
		}

		try (InputStream in = resource.getInputStream();
				OutputStream out = response.getOutputStream();)
		{
			IOUtils.copy(in, out);
		}
	}

	private String buildResourceFromPath(String path)
	{
		if (path == null)
		{
			return null;
		}

		int index = path.indexOf("/");
		Assert.isTrue(index > -1, "Could not get Asset for path " + path);

		String prefix = path.substring(0, index);
		String resource = path.substring(index + 1);

		StringBuilder builder = new StringBuilder();

		if (!prefix.equals("context"))
		{
			builder.append(prefix).append(":");
		}

		builder.append(resource);

		return builder.toString();
	}

	@Autowired
	public void setResourceLoader(LocalizedResourceLoader resourceLoader)
	{
		this.resourceLoader = resourceLoader;
	}

	@Autowired
	public void setLocale(LocaleContext locale)
	{
		this.locale = locale;
	}

	@Override
	public void setServletContext(ServletContext servletContext)
	{
		this.servletContext = servletContext;
	}
}
