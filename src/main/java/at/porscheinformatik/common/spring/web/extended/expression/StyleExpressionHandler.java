package at.porscheinformatik.common.spring.web.extended.expression;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import at.porscheinformatik.common.spring.web.extended.config.ApplicationConfiguration;
import at.porscheinformatik.common.spring.web.extended.template.cache.StackConfig;

public class StyleExpressionHandler implements ExpressionHandler
{
	private StackConfig styleConfig;
	private ApplicationConfiguration config;
	private LinkPreparator linkPreparator;

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
					.append(config.getVersion())
					.append("/style/single/")
					.append(stackName).append("/")
					.append(styleName);

			builder.append(buildStyleLink(prepareHref(href.toString())))
					.append("\n");
		}

		return builder.toString();
	}

	private String buildStyleLink(String href)
	{
		String style = "<link href=\""
				+ href + "\" "
				+ "type=\"text/css\" rel=\"stylesheet\"></link>";

		return style;
	}

	/**
	 * Subclasses may change the styles href for theyr own needs.
	 * 
	 * @param href
	 *            - Link that is used as href of the link tag
	 * @return new Link
	 */
	protected String prepareHref(String href)
	{
		if (linkPreparator != null)
		{
			return linkPreparator.prepareLink(href);
		}

		return href;
	}

	@Autowired(required = false)
	public void setLinkPreparator(LinkPreparator linkPreparator)
	{
		this.linkPreparator = linkPreparator;
	}

	@Override
	public boolean valueNeeded()
	{
		return true;
	}
}
