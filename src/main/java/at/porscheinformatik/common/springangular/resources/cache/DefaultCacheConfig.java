package at.porscheinformatik.common.springangular.resources.cache;

import java.util.HashMap;
import java.util.Map;

public class DefaultCacheConfig implements CacheConfig
{

	private int refreshIntervall = 0;
	private Map<String, CacheEntryConfig> config;
	private String expressionPrefix;
	private String expressionSuffix;
	private String expressionDelimiter;

	public DefaultCacheConfig()
	{
		config = new HashMap<String, CacheEntryConfig>();
	}

	@Override
	public void setRefreshIntervall(int intervall)
	{
		refreshIntervall = intervall;
	}

	@Override
	public int getRefreshIntervall()
	{
		return refreshIntervall;
	}

	@Override
	public void addTemplateConfig(String name, CacheEntryConfig config)
	{
		this.config.put(name, config);
	}

	@Override
	public Map<String, CacheEntryConfig> getTemplateConfig()
	{
		return config;
	}

	public String getExpressionPrefix()
	{
		return expressionPrefix;
	}

	public void setExpressionPrefix(String expressionPrefix)
	{
		this.expressionPrefix = expressionPrefix;
	}

	public String getExpressionSuffix()
	{
		return expressionSuffix;
	}

	public void setExpressionSuffix(String expressionSuffix)
	{
		this.expressionSuffix = expressionSuffix;
	}

	public String getExpressionDelimiter()
	{
		return expressionDelimiter;
	}

	public void setExpressionDelimiter(String expressionDelimiter)
	{
		this.expressionDelimiter = expressionDelimiter;
	}

}
