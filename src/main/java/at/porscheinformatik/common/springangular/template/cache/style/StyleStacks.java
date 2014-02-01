package at.porscheinformatik.common.springangular.template.cache.style;

import at.porscheinformatik.common.springangular.template.cache.DefaultStackConfig;
import at.porscheinformatik.common.springangular.template.cache.StacksBase;

public class StyleStacks extends StacksBase<StyleStack>
{

	public StyleStacks(DefaultStackConfig config)
	{
		super(config);
	}

	@Override
	protected StyleStack createNewInstance()
	{
		return new StyleStack();
	}
}
