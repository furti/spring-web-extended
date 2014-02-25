package at.porscheinformatik.common.spring.web.extended.template.cache.html;

import at.porscheinformatik.common.spring.web.extended.io.ResourceType;
import at.porscheinformatik.common.spring.web.extended.template.cache.DefaultStackConfig;
import at.porscheinformatik.common.spring.web.extended.template.cache.StacksBase;

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
