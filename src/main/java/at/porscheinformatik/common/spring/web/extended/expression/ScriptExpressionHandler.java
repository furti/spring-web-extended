package at.porscheinformatik.common.spring.web.extended.expression;

import java.util.List;

import org.springframework.util.Assert;

import at.porscheinformatik.common.spring.web.extended.config.ApplicationConfiguration;
import at.porscheinformatik.common.spring.web.extended.template.cache.StackConfig;

public class ScriptExpressionHandler extends UrlGeneratingExpressionHandler
{
	private StackConfig scriptConfig;
	private ApplicationConfiguration config;

	public ScriptExpressionHandler(StackConfig scriptConfig,
			ApplicationConfiguration config)
	{
		super();
		this.scriptConfig = scriptConfig;
		this.config = config;
	}

	@Override
	public String process(String value)
	{
		Assert.notNull(scriptConfig, "No scriptstack defined");
		Assert.isTrue(scriptConfig.hasStack(value), "ScriptStack " + value
				+ " not found");

		if (config.isOptimizeResources())
		{
			return buildScriptLink("script/stack", value);
		}
		else
		{
			return buildDevelopmentScripts(value);
		}
	}

	private String buildDevelopmentScripts(String stackName)
	{
		List<String> scriptNames = scriptConfig
				.getResourceNamesForStack(stackName);

		Assert.notEmpty(scriptNames, "No script defined in stack " + stackName);

		StringBuilder builder = new StringBuilder();

		for (String styleName : scriptNames)
		{
			builder.append(
					buildScriptLink("script/single", stackName, styleName))
					.append("\n");
		}

		return builder.toString();
	}

	private String buildScriptLink(String... parts)
	{
		String script = "<script src=\""
				+ generateUrl(parts) + "\" "
				+ "type=\"text/javascript\"></script>";

		return script;
	}

	@Override
	public boolean valueNeeded()
	{
		return true;
	}
}
