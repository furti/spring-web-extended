package at.porscheinformatik.common.spring.web.extended.expression;

import java.util.List;

import org.springframework.util.Assert;

import at.porscheinformatik.common.spring.web.extended.config.ApplicationConfiguration;
import at.porscheinformatik.common.spring.web.extended.template.cache.StackConfig;

public class StyleExpressionHandler extends UrlGeneratingExpressionHandler
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

		if (config.isOptimizeResources())
		{
			return buildStyleLink("style/stack", value);
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
			builder.append(buildStyleLink("style/single", stackName, styleName))
					.append("\n");
		}

		return builder.toString();
	}

	private String buildStyleLink(String... parts)
	{
		String style = "<link href=\""
				+ generateUrl(parts) + "\" "
				+ "type=\"text/css\" rel=\"stylesheet\"></link>";

		return style;
	}

	@Override
	public boolean valueNeeded()
	{
		return true;
	}
}
