package at.porscheinformatik.common.spring.web.extended.config;

public class DefaultApplicationConfiguration implements
		ApplicationConfiguration
{
	private String version;
	private boolean optimizeResources;

	@Override
	public String getVersion()
	{
		return version;
	}

	@Override
	public boolean isOptimizeResources()
	{
		return optimizeResources;
	}

	@Override
	public void setOptimizeResources(boolean optimizeResources)
	{
		this.optimizeResources = optimizeResources;
	}

	@Override
	public void setVersion(String version)
	{
		this.version = version;
	}
}
