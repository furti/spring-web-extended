package at.porscheinformatik.common.springangular.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.ApplicationContext;

import at.porscheinformatik.common.springangular.template.cache.html.HtmlTemplateController;

/**
 * Annotation used to configure the default template controller
 * 
 * @author Daniel Furtlehner
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface HtmlTemplateControllerConfig
{
	/**
	 * If set to true the default {@link HtmlTemplateController} is added to the
	 * {@link ApplicationContext}
	 * 
	 * @return true if the default {@link HtmlTemplateController} should be
	 *         registered, false otherwise
	 */
	boolean registerHtmlTemplateController() default true;

	/**
	 * @return true if a fallback to the index template should be made if the
	 *         requested Template is not found
	 */
	boolean fallbackToIndex() default true;
}
