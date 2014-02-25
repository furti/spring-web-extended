package at.porscheinformatik.common.spring.web.extended.expression;

import java.util.Map;

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
		Assert.notNull(handlers, "No expressionhandlers defined");
		Assert.isTrue(handlers.containsKey(split[0]),
				"Unknown expression prefix " + split[0]);

		return handlers.get(split[0]).process(split[1]);
	}

	private String[] parseExpression(String expression)
	{
		if (expression == null)
		{
			return new String[] {};
		}

		int index = expression.indexOf(":");

		if (index == -1)
		{
			return new String[] {};
		}

		String[] split = new String[2];

		split[0] = expression.substring(0, index);
		split[1] = expression.substring(index + 1);

		return split;
	}
}
