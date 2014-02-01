package at.porscheinformatik.common.springangular.template;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import at.porscheinformatik.common.springangular.resources.cache.template.TemplateCache;
import at.porscheinformatik.common.springangular.util.RequestUtils;
import at.porscheinformatik.common.springangular.util.ResourceNotFoundException;

@Controller
public class TemplateController
{

	private static final Pattern PATH_PATTERN = Pattern
			.compile("^.*template/(.*)");

	private TemplateCache cache;
	private Boolean fallbackToIndex;

	public TemplateController(Boolean fallbackToIndex)
	{
		this.fallbackToIndex = fallbackToIndex != null
				? fallbackToIndex
				: Boolean.TRUE;
	}

	@RequestMapping(value = "**", method = RequestMethod.GET, produces = "text/html")
	@ResponseBody
	public String handleIndex()
	{
		if (!cache.hasTemplate("index"))
		{
			throw new ResourceNotFoundException();
		}

		return cache.renderTemplate("index");
	}

	@RequestMapping(value = "**/template/**", method = RequestMethod.GET, produces = "text/html")
	@ResponseBody
	public String handleTemplate(HttpServletRequest request)
	{
		String path = RequestUtils.getPathFromRegex(request, PATH_PATTERN);

		if (fallbackToIndex.booleanValue() && !cache.hasTemplate(path))
		{
			path = "index";
		}

		if (!cache.hasTemplate(path))
		{
			throw new ResourceNotFoundException();
		}

		return cache.renderTemplate(path);
	}

	@Autowired
	public void setCache(TemplateCache cache)
	{
		this.cache = cache;
	}
}
