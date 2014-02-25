package at.porscheinformatik.common.spring.web.extended.config;

import java.util.List;
import java.util.Map;

import at.porscheinformatik.common.spring.web.extended.expression.ExpressionHandler;
import at.porscheinformatik.common.spring.web.extended.io.ResourceScanner;
import at.porscheinformatik.common.spring.web.extended.locale.LocaleSource;
import at.porscheinformatik.common.spring.web.extended.messagesource.MessageSourceConfig;
import at.porscheinformatik.common.spring.web.extended.template.cache.StackConfig;
import at.porscheinformatik.common.spring.web.extended.template.optimize.OptimizerConfig;

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

	void configureLocaleSources(List<LocaleSource> sources);
}
