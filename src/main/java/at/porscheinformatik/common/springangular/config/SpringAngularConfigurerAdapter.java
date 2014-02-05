package at.porscheinformatik.common.springangular.config;

import java.util.Map;

import at.porscheinformatik.common.springangular.expression.ExpressionHandler;
import at.porscheinformatik.common.springangular.io.ResourceScanner;
import at.porscheinformatik.common.springangular.messagesource.MessageSourceConfig;
import at.porscheinformatik.common.springangular.template.cache.StackConfig;
import at.porscheinformatik.common.springangular.template.optimize.OptimizerConfig;

/**
 * @author Daniel Furtlehner
 * 
 */
public abstract class SpringAngularConfigurerAdapter implements
		SpringAngularConfigurer
{

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

	@Override
	public void configureResourceScanners(Map<String, ResourceScanner> config)
	{
		// Subclasses may override this to add configurations
	}

	@Override
	public void configureTemplateExpressionHandlers(
			Map<String, ExpressionHandler> config)
	{
		// Subclasses may override this to add configurations
	}

	@Override
	public void configureHtmlTemplates(StackConfig htmlConfig)
	{
		// Subclasses may override this to add configurations
	}
}
