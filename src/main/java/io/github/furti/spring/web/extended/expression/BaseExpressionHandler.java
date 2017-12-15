/**
 * 
 */
package io.github.furti.spring.web.extended.expression;

import io.github.furti.spring.web.extended.template.TemplateContext;

/**
 * @author Daniel Furtlehner
 */
public abstract class BaseExpressionHandler implements ExpressionHandler
{

    private final String prefix;

    public BaseExpressionHandler(String prefix)
    {
        super();
        this.prefix = prefix;
    }

    @Override
    public String getPrefix()
    {
        return prefix;
    }

    @Override
    public String process(String value, TemplateContext templateContext)
    {
        //TODO: escape result based on mime type
        return doProcess(value, templateContext);
    }

    /**
     * @param value the expression value
     * @param templateContext the context
     * @return the result of the expression
     */
    protected abstract String doProcess(String value, TemplateContext templateContext);

}
