package at.porscheinformatik.common.springangular.expression;

import java.util.List;

import org.parboiled.Parboiled;
import org.parboiled.parserunners.RecoveringParseRunner;
import org.springframework.beans.factory.annotation.Autowired;

import at.porscheinformatik.common.springangular.expression.parser.InlineTemplateParser;
import at.porscheinformatik.common.springangular.io.ResourceUtils;
import at.porscheinformatik.common.springangular.template.cache.html.HtmlStacks;
import at.porscheinformatik.common.springangular.util.ParboiledUtils;

public class InlineTemplateExpressionHandler implements ExpressionHandler
{
	private HtmlStacks stacks;
	private InlineTemplateParser parser = Parboiled
			.createParser(InlineTemplateParser.class);

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

		RecoveringParseRunner<String> runner = new RecoveringParseRunner<String>(
				parser.inlineTemplate());

		List<String> parts = ParboiledUtils.buildFromResult(
				runner.run(template), template);

		StringBuilder prepared = new StringBuilder();

		for (String part : parts)
		{
			prepared.append(part);
		}

		return prepared.toString();
	}

	@Autowired
	public void setStacks(HtmlStacks stacks)
	{
		this.stacks = stacks;
	}
}
