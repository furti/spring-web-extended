package at.porscheinformatik.common.spring.web.extended.template;

import java.util.Locale;

import at.porscheinformatik.common.spring.web.extended.io.ResourceType;

public class DefaultTemplateRenderContext implements TemplateRenderContext
{
	private Locale locale;
	private ResourceType resourceType;

	public DefaultTemplateRenderContext(Locale locale, ResourceType resourceType)
	{
		super();
		this.locale = locale;
		this.resourceType = resourceType;
	}

	public Locale getLocale()
	{
		return locale;
	}

	public void setLocale(Locale locale)
	{
		this.locale = locale;
	}

	public ResourceType getResourceType()
	{
		return resourceType;
	}

	public void setResourceType(ResourceType resourceType)
	{
		this.resourceType = resourceType;
	}

}
