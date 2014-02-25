package at.porscheinformatik.common.spring.web.extended.config;

import java.util.List;
import java.util.Map;

import at.porscheinformatik.common.spring.web.extended.expression.ExpressionHandler;
import at.porscheinformatik.common.spring.web.extended.io.ResourceScanner;
import at.porscheinformatik.common.spring.web.extended.locale.LocaleSource;
import at.porscheinformatik.common.spring.web.extended.messagesource.MessageSourceConfig;
import at.porscheinformatik.common.spring.web.extended.template.cache.StackConfig;
import at.porscheinformatik.common.spring.web.extended.template.optimize.OptimizerConfig;

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
	public void configureExpressionHandlers(
			Map<String, ExpressionHandler> config)
	{
		// Subclasses may override this to add configurations
	}

	@Override
	public void configureHtmlTemplates(StackConfig htmlConfig)
	{
		// Subclasses may override this to add configurations
	}

	@Override
	public void configureLocaleSources(List<LocaleSource> sources)
	{
		// Subclasses may override this to add configurations
	}
}
