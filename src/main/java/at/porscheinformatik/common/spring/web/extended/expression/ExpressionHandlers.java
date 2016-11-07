package at.porscheinformatik.common.spring.web.extended.expression;

import java.util.Map;
import java.util.Set;

import org.springframework.util.Assert;

public class ExpressionHandlers
{

    protected Map<String, ExpressionHandler> handlers;

    public ExpressionHandlers(Map<String, ExpressionHandler> handlers)
    {
        super();
        this.handlers = handlers;
    }

    public String processExpression(String expression)
    {
        String[] split = parseExpression(expression);
        Assert.isTrue(split.length == 2, "Invalid expression " + expression);

        return processExpression(split[0], split[1]);
    }

    public String processExpression(String handlerName, String value)
    {

        Assert.notNull(handlers, "No expressionhandlers defined");
        Assert.isTrue(handlers.containsKey(handlerName), "Unknown expression prefix " + handlerName);

        return handlers.get(handlerName).process(value);
    }

    private String[] parseExpression(String expression)
    {
        if (expression == null)
        {
            return new String[]{};
        }

        int index = expression.indexOf(":");

        if (index == -1)
        {
            return new String[]{};
        }

        String[] split = new String[2];

        split[0] = expression.substring(0, index);
        split[1] = expression.substring(index + 1);

        return split;
    }

    public Set<String> getHandlerNames()
    {
        return handlers.keySet();
    }

    public ExpressionHandler getHandler(String handlerName)
    {
        return handlers.get(handlerName);
    }
}
