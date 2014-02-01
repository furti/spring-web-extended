package at.porscheinformatik.common.springangular.config;

import java.util.Map;

import at.porscheinformatik.common.springangular.io.ResourceScanner;
import at.porscheinformatik.common.springangular.messagesource.MessageSourceConfig;
import at.porscheinformatik.common.springangular.template.cache.StackConfig;
import at.porscheinformatik.common.springangular.template.cache.TemplateConfig;
import at.porscheinformatik.common.springangular.template.optimize.OptimizerConfig;

public interface SpringAngularConfigurer
{

	/**
	 * The default config is available unter the empty String. This gives you
	 * the ability to remove or modify the default config
	 * 
	 * <p>
	 * A template can be reached under the following url:
	 * <b>http://server/context/template/&lt;configname.lowerCase/&lt;path
	 * starting at the config root&gt;</b>
	 * </p>
	 * 
	 * <p>
	 * E.g. the default configuration is found under the empty string and the
	 * location prefix is context:/WEB-INF/templates
	 * 
	 * Therefore a template under WEB-INF/templates/folder/file.html can be
	 * reached via the url http://server/context/template/folder/file
	 * </p>
	 * 
	 * @param configurations
	 *            config
	 */
	void addTemplateCacheConfig(TemplateConfig configurations);

	void addTemplateResourceScanner(Map<String, ResourceScanner> scanners);

	void configureMessageSource(MessageSourceConfig config);

	void configureStyles(StackConfig config);

	void configureScripts(StackConfig config);

	void configureApplication(ApplicationConfiguration config);

	void configureOptimizers(OptimizerConfig config);

	void configureResourceScanners(Map<String, ResourceScanner> config);
}
