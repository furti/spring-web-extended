package at.porscheinformatik.common.spring.web.extended.util;

public final class SpringAngularUtils
{

	private SpringAngularUtils()
	{

	}

	public static String[] parseExpression(String expression)
	{
		if (expression == null)
		{
			return new String[] {};
		}

		int index = expression.indexOf(":");

		if (index == -1)
		{
			return new String[] { "", expression };
		}

		String[] split = new String[2];

		split[0] = expression.substring(0, index);
		split[1] = expression.substring(index + 1);

		return split;
	}
}
