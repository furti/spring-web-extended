package at.porscheinformatik.common.spring.web.extended.template.cache;


public class DefaultTemplateEntryConfig implements TemplateEntryConfig
{

	private String locationPrefix;

	public DefaultTemplateEntryConfig(String locationPrefix)
	{
		super();
		this.locationPrefix = locationPrefix;
	}

	public String getLocationPrefix()
	{
		return locationPrefix;
	}

	public void setLocationPrefix(String locationPrefix)
	{
		this.locationPrefix = locationPrefix;
	}
}
