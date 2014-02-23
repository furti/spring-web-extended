package at.porscheinformatik.common.springangular.annotation;

//CHECKSTYLE:OFF
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import at.porscheinformatik.common.springangular.config.SpringAngularConfig;
import at.porscheinformatik.common.springangular.config.SpringAngularConfigurer;
import at.porscheinformatik.common.springangular.config.SpringAngularConfigurerAdapter;
import at.porscheinformatik.common.springangular.config.SpringAngularRegistrar;
import at.porscheinformatik.common.springangular.template.cache.html.HtmlTemplateController;

//CHECKSTYLE:ON

/**
 * Add this annotation to @{@link Configuration} classes to enable Angular
 * Support configured in {@link SpringAngularConfig} and
 * {@link SpringAngularRegistrar}.
 * 
 * <p>
 * You can customize the configuration by implementing the
 * {@link SpringAngularConfigurer} or extending the
 * {@link SpringAngularConfigurerAdapter}
 * </p>
 * 
 * <pre>
 * &#064;Configuration
 * &#064;EnableSpringAngular
 * &#064;ComponentScan(basePackageClasses = { AppConfig.class })
 * public class AppConfig extends SpringAngularConfigurerAdapter
 * {
 * 	&#064;Override
 * 	public void configureMessageSource(MessageSourceConfig config)
 * 	{
 * 		config.addBaseName(&quot;path/to/my/messages&quot;)
 * 	}
 * }
 * </pre>
 * 
 * @author Daniel Furtlehner
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(value = { SpringAngularConfig.class, SpringAngularRegistrar.class })
@EnableWebMvc
public @interface EnableSpringAngular
{

	/**
	 * @return the configuration for the {@link HtmlTemplateController}
	 */
	HtmlTemplateControllerConfig htmlTemplateControllerConfig() default @HtmlTemplateControllerConfig;

	/**
	 * @return true if the default Assetcontroller should be registered
	 */
	boolean registerAssetController() default true;

	/**
	 * @return true if the default Stylecontroller should be registered
	 */
	boolean registerStyleController() default true;

	/**
	 * @return true if the default Scriptcontroller should be registered
	 */
	boolean registerScriptController() default true;
}
