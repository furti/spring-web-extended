package at.porscheinformatik.common.springangular.expression;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;

import at.porscheinformatik.common.springangular.io.ResourceUtils;
import at.porscheinformatik.common.springangular.template.cache.html.HtmlStacks;

public class InlineTemplateExpressionHandler implements ExpressionHandler
{
	private static final Pattern NEWLINE = Pattern.compile("\\r|\\n");

	private HtmlStacks stacks;

	@Override
	public String process(String value)
	{
		String templateName = value.toLowerCase() + ".html";

		if (isTemplate("", templateName))
		{
			return prepareResult(stacks.get("").renderTemplate(templateName));
		}

		String[] pathAndFile = ResourceUtils.pathAndFile(templateName);

		if (isTemplate(pathAndFile[0], pathAndFile[1]))
		{
			return prepareResult(stacks.get(pathAndFile[0]).renderTemplate(
					pathAndFile[1]));
		}

		throw new IllegalArgumentException("Template " + value
				+ " not found");
	}

	private boolean isTemplate(String stackName, String templateName)
	{
		return stacks.hasStack(stackName)
				&& stacks.get(stackName).hasTemplate(templateName);
	}

	private String prepareResult(String template)
	{
		if (template == null)
		{
			return null;
		}

		Matcher m = NEWLINE.matcher(template);
		// TODO: tabs noch rauslöschen am zeilenanfang.

		return m.replaceAll("");
	}

	@Autowired
	public void setStacks(HtmlStacks stacks)
	{
		this.stacks = stacks;
	}

}
