package io.github.furti.spring.web.extended.expression;

import org.apache.commons.lang.StringEscapeUtils;

import io.github.furti.spring.web.extended.template.TemplateRenderContext;
import io.github.furti.spring.web.extended.template.TemplateRenderContextHolder;

public abstract class BaseExpressionHandler implements ExpressionHandler
{
    private final boolean valueNeeded;

    public BaseExpressionHandler(boolean valueNeeded)
    {
        super();
        this.valueNeeded = valueNeeded;
    }

    @Override
    public String process(String value)
    {
        return escape(doProcess(value));
    }

    protected abstract String doProcess(String value);

    protected TemplateRenderContext getTemplateRenderContext()
    {
        return TemplateRenderContextHolder.actualContext();
    }

    private String escape(String value)
    {
        TemplateRenderContext context = getTemplateRenderContext();

        if (context == null || context.getResourceType() == null)
        {
            return value;
        }

        switch (context.getResourceType())
        {
            case HTML:
                return StringEscapeUtils.escapeHtml(value);
            case SCRIPT:
                return StringEscapeUtils.escapeJavaScript(value);
            default:
                return value;
        }
    }

    @Override
    public boolean valueNeeded()
    {
        return valueNeeded;
    }
}
