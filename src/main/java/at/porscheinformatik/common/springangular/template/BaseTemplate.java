package at.porscheinformatik.common.springangular.template;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.springframework.util.StringUtils;

import at.porscheinformatik.common.springangular.io.ResourceType;
import at.porscheinformatik.common.springangular.template.optimize.OptimizerChain;

public abstract class BaseTemplate implements Template
{

	private OptimizerChain optimizerChain;
	private ResourceType type;
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private ReadLock readLock = lock.readLock();
	private WriteLock writeLock = lock.writeLock();

	public BaseTemplate(ResourceType type)
			throws IOException
	{
		this.type = type;
	}

	public String render() throws IOException
	{
		readLock.lock();
		try
		{
			String template = getContent();

			if (!StringUtils.hasText(template))
			{
				return template;
			}

			if (optimizerChain != null)
			{
				return optimizerChain.optimize(type, getTemplateUri(),
						template);
			}
			else
			{
				return template;
			}
		} finally
		{
			readLock.unlock();
		}
	}

	@Override
	public boolean isChanged(Date since) throws IOException
	{
		long lastmodified = getLastModified();

		return lastmodified > since.getTime();
	}

	@Override
	public void refresh() throws IOException
	{
		writeLock.lock();
		try
		{
			doRefresh();
		} finally
		{
			writeLock.unlock();
		}
	}

	public void setOptimizerChain(OptimizerChain optimizerChain)
	{
		this.optimizerChain = optimizerChain;
	}

	protected abstract String getTemplateUri();

	protected abstract long getLastModified() throws IOException;

	protected abstract String getContent() throws IOException;

	protected abstract void doRefresh() throws IOException;
}
