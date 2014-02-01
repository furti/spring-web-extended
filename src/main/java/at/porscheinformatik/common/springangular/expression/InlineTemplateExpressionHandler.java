package at.porscheinformatik.common.springangular.expression;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import at.porscheinformatik.common.springangular.template.cache.html.HtmlTemplateCache;

public class InlineTemplateExpressionHandler implements ExpressionHandler
{
	private static final Pattern NEWLINE = Pattern.compile("\\r|\\n");

	private HtmlTemplateCache templateCache;

	@Override
	public String process(String value)
	{
		Assert.isTrue(templateCache.hasTemplate(value), "Template " + value
				+ " not found");

		return prepareResult(templateCache.renderTemplate(value));
	}

	private String prepareResult(String template)
	{
		if (template == null)
		{
			return null;
		}

		Matcher m = NEWLINE.matcher(template);

		return m.replaceAll("");
	}

	@Autowired
	public void setTemplateCache(HtmlTemplateCache templateCache)
	{
		this.templateCache = templateCache;
	}

}
