/**
 * 
 */
package io.github.furti.spring.web.extended.template;

import java.nio.charset.Charset;

import org.springframework.core.io.Resource;

/**
 * @author Daniel Furtlehner
 */
public interface TemplateFactory
{
    Template createTemplate(Resource resource, TemplateContext context, Charset charset);
}
