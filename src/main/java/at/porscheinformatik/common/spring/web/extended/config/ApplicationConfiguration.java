package at.porscheinformatik.common.spring.web.extended.config;

public interface ApplicationConfiguration
{

	String getVersion();

	void setVersion(String version);

	boolean isOptimizeResources();

	void setOptimizeResources(boolean optimizeResources);
}
