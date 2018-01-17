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
    private final ContentEscapeHandlerRegistry escapeHandlers;
    private final char expressionStart;
    private final char expressionDelimiter;
    private final char expressionEnd;

    public SimpleTemplateFactory(ExpressionHandlerRegistry expressionHandlers,
        ContentEscapeHandlerRegistry escapeHandlers, char expressionStart, char expressionDelimiter, char expressionEnd)
    {
        super();
        this.expressionHandlers = expressionHandlers;
        this.escapeHandlers = escapeHandlers;
        this.expressionStart = expressionStart;
        this.expressionDelimiter = expressionDelimiter;
        this.expressionEnd = expressionEnd;
    }

    @Override
    public Template createTemplate(Resource resource, TemplateContext context, Charset charset)
    {
        return new SimpleTemplate(resource, context, charset, this.expressionHandlers, escapeHandlers, expressionStart,
            expressionDelimiter, expressionEnd);
    }

}
