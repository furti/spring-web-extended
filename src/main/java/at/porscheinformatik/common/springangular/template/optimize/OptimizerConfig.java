package at.porscheinformatik.common.springangular.template.optimize;

import java.util.List;

import ro.isdc.wro.model.resource.processor.ResourcePreProcessor;
import at.porscheinformatik.common.springangular.io.ResourceType;

public interface OptimizerConfig
{
	// TODO: hier sollte noch die möglichkeit rein das man die optimizer
	// sortieren kann. Sowas wie "before:anotheroptimizer"
	/*
	 * TODO: denke wir brauchen hier keinen Namen. So wie SpringSecurity das
	 * macht mit before <Klasse des Optimizers> müsste es funktionieren.
	 */
	void addOptimizer(ResourceType type, String name,
			ResourcePreProcessor processor);

	// TODO: möglichkeit Optimizer zu entfernen.

	List<ResourcePreProcessor> getProcessors(ResourceType type);
}
