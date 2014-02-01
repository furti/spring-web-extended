package at.porscheinformatik.common.springangular.config;

import java.util.Map;

import at.porscheinformatik.common.springangular.messagesource.MessageSourceConfig;
import at.porscheinformatik.common.springangular.resources.ResourceScanner;
import at.porscheinformatik.common.springangular.resources.cache.CacheConfig;
import at.porscheinformatik.common.springangular.resources.cache.StackConfig;
import at.porscheinformatik.common.springangular.resources.optimize.OptimizerConfig;

/**
 * @author Daniel Furtlehner
 * 
 */
public abstract class SpringAngularConfigurerAdapter implements
		SpringAngularConfigurer
{

	@Override
	public void addTemplateCacheConfig(CacheConfig configuration)
	{
		// Subclasses may override this to add configurations
	}

	@Override
	public void addTemplateResourceScanner(
			Map<String, ResourceScanner> scanners)
	{
		// Subclasses may override this to add configurations
	}

	@Override
	public void configureMessageSource(MessageSourceConfig config)
	{
		// Subclasses may override this to add configurations
	}

	@Override
	public void configureStyles(StackConfig config)
	{
		// Subclasses may override this to add configurations
	}

	@Override
	public void configureScripts(StackConfig config)
	{
		// Subclasses may override this to add configurations
	}

	@Override
	public void configureApplication(ApplicationConfiguration config)
	{
		// Subclasses may override this to add configurations
	}

	@Override
	public void configureOptimizers(OptimizerConfig config)
	{
		// Subclasses may override this to add configurations
	}

}
