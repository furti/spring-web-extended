package at.porscheinformatik.common.springangular.template.cache.html;

import at.porscheinformatik.common.springangular.io.ResourceType;
import at.porscheinformatik.common.springangular.template.cache.DefaultStackConfig;
import at.porscheinformatik.common.springangular.template.cache.StacksBase;

public class HtmlStacks extends StacksBase<HtmlStack>
{
	public HtmlStacks(DefaultStackConfig config)
	{
		super(config);
	}

	@Override
	protected HtmlStack createNewInstance()
	{
		return new HtmlStack(ResourceType.HTML);
	}
}
