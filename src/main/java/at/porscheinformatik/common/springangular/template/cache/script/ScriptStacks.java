package at.porscheinformatik.common.springangular.template.cache.script;

import at.porscheinformatik.common.springangular.template.cache.DefaultStackConfig;
import at.porscheinformatik.common.springangular.template.cache.StacksBase;

public class ScriptStacks extends StacksBase<ScriptStack>
{

	public ScriptStacks(DefaultStackConfig config)
	{
		super(config);
	}

	@Override
	protected ScriptStack createNewInstance()
	{
		return new ScriptStack();
	}

}
