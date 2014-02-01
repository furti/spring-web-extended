package at.porscheinformatik.common.springangular.config;

public interface ApplicationConfiguration
{

	String getVersion();

	void setVersion(String version);

	boolean isOptimizeResources();

	void setOptimizeResources(boolean optimizeResources);
}
