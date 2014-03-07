package at.porscheinformatik.common.spring.web.extended.expression;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import at.porscheinformatik.common.spring.web.extended.util.SpringWebExtendedUtils;

/**
 * Produces a realtive url for an asset
 * 
 * @author Daniel Furtlehner
 * 
 */
public class AssetExpressionHandler extends UrlGeneratingExpressionHandler
{
	@Override
	public String process(String value)
	{
		String[] split = SpringWebExtendedUtils.parseExpression(value);
		Assert.isTrue(split.length == 2, "Invalid asset " + value);

		String[] urlParts = new String[3];
		urlParts[0] = "asset";

		if (StringUtils.hasText(split[0]))
		{
			urlParts[1] = split[0];
		}
		else
		{
			urlParts[1] = "context";
		}

		urlParts[2] = split[1];

		return generateUrl(urlParts);
	}

	@Override
	public boolean valueNeeded()
	{
		return true;
	}
}
