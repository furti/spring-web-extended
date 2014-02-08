package at.porscheinformatik.common.springangular.config;

import java.util.Map;

import at.porscheinformatik.common.springangular.expression.ExpressionHandler;
import at.porscheinformatik.common.springangular.io.ResourceScanner;
import at.porscheinformatik.common.springangular.messagesource.MessageSourceConfig;
import at.porscheinformatik.common.springangular.template.cache.StackConfig;
import at.porscheinformatik.common.springangular.template.optimize.OptimizerConfig;

public interface SpringAngularConfigurer
{
	void configureMessageSource(MessageSourceConfig config);

	void configureStyles(StackConfig config);

	void configureScripts(StackConfig config);

	void configureHtmlTemplates(StackConfig config);

	void configureApplication(ApplicationConfiguration config);

	void configureOptimizers(OptimizerConfig config);

	void configureResourceScanners(Map<String, ResourceScanner> config);

	void configureExpressionHandlers(Map<String, ExpressionHandler> config);
}
