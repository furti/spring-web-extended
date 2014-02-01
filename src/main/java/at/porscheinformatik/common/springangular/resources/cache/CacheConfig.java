package at.porscheinformatik.common.springangular.resources.cache;

import java.util.Map;

public interface CacheConfig
{
	/**
	 * If the intervall is less than 1 no refresh will be performed.
	 * 
	 * @param intervall
	 *            in seconds.
	 */
	void setRefreshIntervall(int intervall);

	/**
	 * Defaults to 5 seconds
	 * 
	 * @return intervall in seconds. -1 for no refresh
	 */
	int getRefreshIntervall();

	void addTemplateConfig(String name, CacheEntryConfig config);

	Map<String, CacheEntryConfig> getTemplateConfig();

	String getExpressionPrefix();

	String getExpressionSuffix();

	String getExpressionDelimiter();
}
