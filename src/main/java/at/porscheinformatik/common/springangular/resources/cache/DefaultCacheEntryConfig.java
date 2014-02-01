package at.porscheinformatik.common.springangular.resources.cache;

public class DefaultCacheEntryConfig implements CacheEntryConfig
{

	private String locationPrefix;

	public DefaultCacheEntryConfig(String locationPrefix)
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
