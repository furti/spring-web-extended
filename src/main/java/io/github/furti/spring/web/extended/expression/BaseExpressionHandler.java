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
    private final boolean valueRequired;

    public BaseExpressionHandler(String prefix, boolean valueRequired)
    {
        super();

        this.prefix = prefix;
        this.valueRequired = valueRequired;
    }

    @Override
    public String getPrefix()
    {
        return prefix;
    }

    @Override
    public String process(String value, TemplateContext templateContext)
    {
        return doProcess(value, templateContext);
    }

    @Override
    public boolean isValueRequired()
    {
        return valueRequired;
    }

    /**
     * @param value the expression value
     * @param templateContext the context
     * @return the result of the expression
     */
    protected abstract String doProcess(String value, TemplateContext templateContext);

}
