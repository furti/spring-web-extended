package at.porscheinformatik.common.spring.web.extended.expression;

import java.util.List;

import org.parboiled.Parboiled;
import org.parboiled.parserunners.RecoveringParseRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import at.porscheinformatik.common.spring.web.extended.expression.parser.InlineTemplateParser;
import at.porscheinformatik.common.spring.web.extended.io.ResourceUtils;
import at.porscheinformatik.common.spring.web.extended.template.cache.html.HtmlStacks;
import at.porscheinformatik.common.spring.web.extended.util.ParboiledUtils;

public class InlineTemplateExpressionHandler implements ExpressionHandler,
		BeanFactoryAware
{
	private Logger logger = LoggerFactory.getLogger(getClass());

	private HtmlStacks stacks;
	private BeanFactory beanFactory;

	@Override
	public String process(String value)
	{
		logger.info("Rendering inlinetemplate " + value);

		HtmlStacks htmlStacks = getStacks();

		String templateName = value.toLowerCase() + ".html";

		if (isTemplate("", templateName, htmlStacks))
		{
			return prepareResult(htmlStacks.get("")
					.renderTemplate(templateName));
		}

		String[] pathAndFile = ResourceUtils.pathAndFile(templateName);

		if (isTemplate(pathAndFile[0], pathAndFile[1], htmlStacks))
		{
			return prepareResult(htmlStacks.get(pathAndFile[0]).renderTemplate(
					pathAndFile[1]));
		}

		throw new IllegalArgumentException("Template " + value
				+ " not found");
	}

	private boolean isTemplate(String stackName, String templateName,
			HtmlStacks htmlStacks)
	{
		return htmlStacks.hasStack(stackName)
				&& htmlStacks.get(stackName).hasTemplate(templateName);
	}

	private String prepareResult(String template)
	{
		if (template == null)
		{
			return null;
		}

		InlineTemplateParser parser = Parboiled
				.createParser(InlineTemplateParser.class);

		RecoveringParseRunner<String> runner = new RecoveringParseRunner<String>(
				parser.inlineTemplate());

		try
		{
			List<String> parts = ParboiledUtils.buildFromResult(
					runner.run(template), template);

			StringBuilder prepared = new StringBuilder();

			for (String part : parts)
			{
				prepared.append(part);
			}

			return prepared.toString();
		} catch (Exception ex)
		{
			logger.error("Error rendering inline template " + template, ex);
			throw ex;
		}
	}

	private HtmlStacks getStacks()
	{
		// We need this because of the circular dependency between handler and
		// stack
		if (stacks == null)
		{
			stacks = beanFactory.getBean(HtmlStacks.class);
		}

		return stacks;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException
	{
		this.beanFactory = beanFactory;
	}
}
