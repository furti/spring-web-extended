package at.porscheinformatik.common.spring.web.extended.template.cache.script;

import at.porscheinformatik.common.spring.web.extended.template.cache.DefaultStackConfig;
import at.porscheinformatik.common.spring.web.extended.template.cache.StacksBase;

public class ScriptStacks extends StacksBase<ScriptStack>
{

	public ScriptStacks(DefaultStackConfig config)
	{
		super(config);
	}

	@Override
	protected ScriptStack createNewInstance(String stackName)
	{
		return new ScriptStack(stackName);
	}

}
