/**
 * 
 */
package io.github.furti.spring.web.extended.template.simple;

import java.nio.charset.Charset;

import org.springframework.core.io.Resource;

import io.github.furti.spring.web.extended.expression.ExpressionHandlerRegistry;
import io.github.furti.spring.web.extended.template.Template;
import io.github.furti.spring.web.extended.template.TemplateContext;
import io.github.furti.spring.web.extended.template.TemplateFactory;

/**
 * @author Daniel Furtlehner
 */
public class SimpleTemplateFactory implements TemplateFactory
{

    private final ExpressionHandlerRegistry expressionHandlers;

    public SimpleTemplateFactory(ExpressionHandlerRegistry expressionHandlers)
    {
        this.expressionHandlers = expressionHandlers;
    }

    @Override
    public Template createTemplate(Resource resource, TemplateContext context, Charset charset)
    {
        return new SimpleTemplate(resource, context, charset, this.expressionHandlers);
    }

}
