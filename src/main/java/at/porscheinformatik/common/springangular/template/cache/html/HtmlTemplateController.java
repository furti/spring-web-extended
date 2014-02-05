package at.porscheinformatik.common.springangular.template.cache.html;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import at.porscheinformatik.common.springangular.io.ResourceUtils;
import at.porscheinformatik.common.springangular.util.RequestUtils;
import at.porscheinformatik.common.springangular.util.ResourceNotFoundException;

@Controller
public class HtmlTemplateController
{
	private static final String INDEX = "index.html";

	private static final Pattern PATH_PATTERN = Pattern
			.compile("^.*template/(.*)");

	private HtmlStacks stacks;
	private Boolean fallbackToIndex;

	public HtmlTemplateController(Boolean fallbackToIndex)
	{
		this.fallbackToIndex = fallbackToIndex != null
				? fallbackToIndex
				: Boolean.TRUE;
	}

	@RequestMapping(value = "**", method = RequestMethod.GET, produces = "text/html")
	@ResponseBody
	public String handleIndex()
	{
		if (!indexAvaliable())
		{
			throw new ResourceNotFoundException();
		}

		return stacks.get("").renderTemplate(INDEX);
	}

	@RequestMapping(value = "**/template/**", method = RequestMethod.GET, produces = "text/html")
	@ResponseBody
	public String handleTemplate(HttpServletRequest request)
	{
		String path = RequestUtils.getPathFromRegex(request, PATH_PATTERN)
				.toLowerCase() + ".html";
		if (isDefaultTemplate(path))
		{
			return stacks.get("").renderTemplate(path);
		}
		else
		{
			String[] pathAndFile = ResourceUtils.pathAndFile(path);

			if (StringUtils.hasText(pathAndFile[0]))
			{
				if (isTemplate(pathAndFile[0], pathAndFile[1]))
				{
					return stacks.get(pathAndFile[0]).renderTemplate(
							pathAndFile[1]);
				}
			}
		}

		if (fallbackToIndex.booleanValue() && indexAvaliable())
		{
			return stacks.get("").renderTemplate(INDEX);
		}

		throw new ResourceNotFoundException();
	}

	private boolean indexAvaliable()
	{
		return isDefaultTemplate(INDEX);
	}

	private boolean isDefaultTemplate(String templateName)
	{
		return isTemplate("", templateName);
	}

	private boolean isTemplate(String stackName, String templateName)
	{
		return stacks.hasStack("")
				&& stacks.get("").hasTemplate(templateName);
	}

	@Autowired
	public void setStacks(HtmlStacks stacks)
	{
		this.stacks = stacks;
	}
}
