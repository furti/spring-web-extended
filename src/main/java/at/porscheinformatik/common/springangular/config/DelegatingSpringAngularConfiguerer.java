package at.porscheinformatik.common.springangular.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import at.porscheinformatik.common.springangular.messagesource.MessageSourceConfig;
import at.porscheinformatik.common.springangular.resources.ResourceScanner;
import at.porscheinformatik.common.springangular.resources.cache.CacheConfig;
import at.porscheinformatik.common.springangular.resources.cache.StackConfig;
import at.porscheinformatik.common.springangular.resources.optimize.OptimizerConfig;

public class DelegatingSpringAngularConfiguerer implements
		SpringAngularConfigurer
{

	private List<SpringAngularConfigurer> configurers = new ArrayList<>();

	public void addConfigurers(Collection<SpringAngularConfigurer> configurers)
	{
		if (configurers != null)
		{
			this.configurers.addAll(configurers);
		}
	}

	@Override
	public void addTemplateCacheConfig(CacheConfig configurations)
	{
		for (SpringAngularConfigurer configurer : configurers)
		{
			configurer.addTemplateCacheConfig(configurations);
		}
	}

	@Override
	public void addTemplateResourceScanner(
			Map<String, ResourceScanner> scanners)
	{
		for (SpringAngularConfigurer configurer : configurers)
		{
			configurer.addTemplateResourceScanner(scanners);
		}
	}

	@Override
	public void configureMessageSource(MessageSourceConfig config)
	{
		for (SpringAngularConfigurer configurer : configurers)
		{
			configurer.configureMessageSource(config);
		}
	}

	@Override
	public void configureStyles(StackConfig config)
	{
		for (SpringAngularConfigurer configurer : configurers)
		{
			configurer.configureStyles(config);
		}
	}

	@Override
	public void configureScripts(StackConfig config)
	{
		for (SpringAngularConfigurer configurer : configurers)
		{
			configurer.configureScripts(config);
		}
	}

	@Override
	public void configureApplication(ApplicationConfiguration config)
	{
		for (SpringAngularConfigurer configurer : configurers)
		{
			configurer.configureApplication(config);
		}
	}

	@Override
	public void configureOptimizers(OptimizerConfig config)
	{
		for (SpringAngularConfigurer configurer : configurers)
		{
			configurer.configureOptimizers(config);
		}
	}
}
