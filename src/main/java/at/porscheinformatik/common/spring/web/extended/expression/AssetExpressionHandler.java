package at.porscheinformatik.common.spring.web.extended.expression;

import at.porscheinformatik.common.spring.web.extended.util.AssetUtils;

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
		String[] assetParts = AssetUtils.createAssetParts(value);

		return generateUrl(assetParts);
	}

	@Override
	public boolean valueNeeded()
	{
		return true;
	}
}
