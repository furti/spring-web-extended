package at.porscheinformatik.common.springangular.expression;

import java.util.List;

import org.springframework.util.Assert;

import at.porscheinformatik.common.springangular.config.ApplicationConfiguration;
import at.porscheinformatik.common.springangular.template.cache.StackConfig;

public class StyleExpressionHandler implements ExpressionHandler
{

	private StackConfig styleConfig;
	private ApplicationConfiguration config;

	public StyleExpressionHandler(StackConfig styleConfig,
			ApplicationConfiguration config)
	{
		super();
		this.styleConfig = styleConfig;
		this.config = config;
	}

	@Override
	public String process(String value)
	{
		Assert.notNull(styleConfig, "No stylestack defined");
		Assert.isTrue(styleConfig.hasStack(value), "StyleStack " + value
				+ " not found");

		/*
		 * TODO: die ApplicationVersion sollte noch in die URL eingefügt werden
		 * damit der Browser bei einer neuen Version der Applikation die Styles neu lädt.
		 */
		
		if (config.isOptimizeResources())
		{
			return buildStyleLink("style/stack/" + value);
		}
		else
		{
			return buildDevelopmentStyles(value);
		}
	}

	private String buildDevelopmentStyles(String stackName)
	{
		List<String> styleNames = styleConfig
				.getResourceNamesForStack(stackName);

		Assert.notEmpty(styleNames, "No styles defined in stack " + stackName);

		StringBuilder builder = new StringBuilder();

		for (String styleName : styleNames)
		{
			StringBuilder href = new StringBuilder()
					.append("style/single/")
					.append(stackName).append("/")
					.append(styleName);

			builder.append(buildStyleLink(href.toString())).append("\n");
		}

		return builder.toString();
	}

	private String buildStyleLink(String href)
	{
		String style = "<link href=\"" + href + "\" "
				+ "type=\"text/css\" rel=\"stylesheet\"></link>";

		return style;
	}
}
