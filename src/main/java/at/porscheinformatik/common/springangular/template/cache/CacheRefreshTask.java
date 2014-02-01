package at.porscheinformatik.common.springangular.template.cache;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheRefreshTask implements Runnable
{
	private static final Logger LOG = LoggerFactory
			.getLogger(CacheRefreshTask.class);
	private AbstractTemplateCache cache;

	public CacheRefreshTask(AbstractTemplateCache cache)
	{
		super();
		this.cache = cache;
	}

	@Override
	public void run()
	{
		try
		{
			cache.refreshTemplates();
		} catch (IOException ex)
		{
			LOG.error("Error refreshing Templates", ex);
		}
	}
}
