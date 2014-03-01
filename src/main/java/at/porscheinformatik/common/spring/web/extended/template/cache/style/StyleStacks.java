package at.porscheinformatik.common.spring.web.extended.template.cache.style;

import at.porscheinformatik.common.spring.web.extended.template.cache.DefaultStackConfig;
import at.porscheinformatik.common.spring.web.extended.template.cache.StacksBase;

public class StyleStacks extends StacksBase<StyleStack>
{

	public StyleStacks(DefaultStackConfig config)
	{
		super(config);
	}

	@Override
	protected StyleStack createNewInstance(String stackName)
	{
		return new StyleStack(stackName);
	}
}
