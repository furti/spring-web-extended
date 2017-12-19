/**
 * 
 */
package io.github.furti.spring.web.extended.template.simple;

import io.github.furti.spring.web.extended.expression.ExpressionHandler;
import io.github.furti.spring.web.extended.template.TemplateContext;

/**
 * @author Daniel Furtlehner
 */
public class ExpressionPart implements TemplatePart
{
    private final String originalExpression;
    private final ExpressionHandler expressionHandler;
    private final String value;

    public ExpressionPart(String originalExpression, ExpressionHandler expressionHandler, String value)
    {
        super();
        this.originalExpression = originalExpression;
        this.expressionHandler = expressionHandler;
        this.value = value;
    }

    @Override
    public String render(TemplateContext context)
    {
        String result = expressionHandler.process(value, context);

        /*
         * When the result is null we assume that we encountered something that looks like an expression 
         * but does not perform like an expression. So it might be no expression at all.
         */
        if (result == null)
        {
            return originalExpression;
        }

        return result;
    }

}
