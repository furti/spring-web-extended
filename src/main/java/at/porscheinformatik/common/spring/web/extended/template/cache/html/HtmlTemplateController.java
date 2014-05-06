/**
 * Copyright 2014 Daniel Furtlehner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.porscheinformatik.common.spring.web.extended.template.cache.html;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import at.porscheinformatik.common.spring.web.extended.io.ResourceUtils;
import at.porscheinformatik.common.spring.web.extended.util.RequestUtils;
import at.porscheinformatik.common.spring.web.extended.util.ResourceNotFoundException;

@Controller
public class HtmlTemplateController
{
	private static final String INDEX = "index.html";

	private static final Pattern PATH_PATTERN = Pattern
			.compile("^.*template/(.*)");

	protected HtmlStacks stacks;
	private Boolean fallbackToIndex;

	public HtmlTemplateController(Boolean fallbackToIndex)
	{
		this.fallbackToIndex = fallbackToIndex != null
				? fallbackToIndex
				: Boolean.TRUE;
	}

	@RequestMapping(value = "**", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	@ResponseBody
	public String handleIndex()
	{
		if (!indexAvaliable())
		{
			throw new ResourceNotFoundException();
		}

		return renderDefaultTemplate(INDEX);
	}

	@RequestMapping(value = "**/template/**", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	@ResponseBody
	public String handleTemplate(HttpServletRequest request)
	{
		String path = RequestUtils.getPathFromRegex(request, PATH_PATTERN)
				.toLowerCase() + ".html";
		if (isDefaultTemplate(path))
		{
			return renderDefaultTemplate(path);
		}
		else
		{
			String[] pathAndFile = ResourceUtils.pathAndFile(path);

			if (StringUtils.hasText(pathAndFile[0]))
			{
				if (isTemplate(pathAndFile[0], pathAndFile[1]))
				{
					return renderTemplate(pathAndFile[0], pathAndFile[1]);
				}
			}
		}

		if (fallbackToIndex.booleanValue() && indexAvaliable())
		{
			return renderDefaultTemplate(INDEX);
		}

		throw new ResourceNotFoundException();
	}

	private boolean indexAvaliable()
	{
		return isDefaultTemplate(INDEX);
	}

	protected boolean isDefaultTemplate(String templateName)
	{
		return isTemplate("", templateName);
	}

	protected boolean isTemplate(String stackName, String templateName)
	{
		return stacks.hasStack("")
				&& stacks.get("").hasTemplate(templateName);
	}

	protected String renderDefaultTemplate(String templateName)
	{
		return renderTemplate("", templateName);
	}

	protected String renderTemplate(String stackName, String templateName)
	{
		return stacks.get("").renderTemplate(templateName);
	}

	@Autowired
	public void setStacks(HtmlStacks stacks)
	{
		this.stacks = stacks;
	}
}
