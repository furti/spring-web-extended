package io.github.furti.spring.web.extended.template.legacy.part;

import org.springframework.util.Assert;

import io.github.furti.spring.web.extended.expression.legacy.ExpressionHandlers;

public class ExpressionPart implements TemplatePart
{

    private final String expression;
    private final ExpressionHandlers handlers;

    public ExpressionPart(String handler, String value, ExpressionHandlers handlers)
    {
        this(handler + ":" + value, handlers);
    }

    public ExpressionPart(String expression, ExpressionHandlers handlers)
    {
        super();
        Assert.notNull(handlers, "Handlers must be set");
        Assert.notNull(expression, "Expression must not be null");
        this.expression = expression;
        this.handlers = handlers;
    }

    @Override
    public String render()
    {
        return handlers.processExpression(expression);
    }

    @Override
    public String toString()
    {
        return "${" + expression + "}";
    }
}
