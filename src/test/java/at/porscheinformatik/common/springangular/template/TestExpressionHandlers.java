package at.porscheinformatik.common.springangular.template;

import java.util.HashMap;

import at.porscheinformatik.common.springangular.expression.ExpressionHandler;
import at.porscheinformatik.common.springangular.expression.ExpressionHandlers;

public class TestExpressionHandlers extends ExpressionHandlers
{

	public TestExpressionHandlers()
	{
		super(new HashMap<String, ExpressionHandler>());
		handlers.put("simple", new SimpleExpressionHandler());
	}

	private static class SimpleExpressionHandler implements ExpressionHandler
	{

		@Override
		public String process(String value)
		{
			return value;
		}

	}

}
