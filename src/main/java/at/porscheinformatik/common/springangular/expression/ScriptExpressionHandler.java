package at.porscheinformatik.common.springangular.expression;

import java.util.List;

import org.springframework.util.Assert;

import at.porscheinformatik.common.springangular.config.ApplicationConfiguration;
import at.porscheinformatik.common.springangular.template.cache.StackConfig;

public class ScriptExpressionHandler implements ExpressionHandler
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
			return buildScriptLink("script/stack/" + value);
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
			StringBuilder href = new StringBuilder()
					.append("script/single/")
					.append(stackName).append("/")
					.append(styleName);

			builder.append(buildScriptLink(href.toString())).append("\n");
		}

		return builder.toString();
	}

	private String buildScriptLink(String href)
	{
		String script = "<script src=\"" + href + "\" "
				+ "type=\"text/javascript\"></script>";

		return script;
	}
}
