package at.porscheinformatik.common.spring.web.extended.expression;

import at.porscheinformatik.common.spring.web.extended.util.AssetUtils;

/**
 * Produces a realtive url for an asset Resource Handler names must be separated from the content with a # in asset
 * expressions. Because the chunk template engine strips everything after ":" or "|". So we won't get the full path if
 * we use the default separator ":".
 * 
 * @author Daniel Furtlehner
 */
public class AssetExpressionHandler extends UrlGeneratingExpressionHandler
{
    @Override
    public String process(String value)
    {
        String[] assetParts = AssetUtils.createAssetParts(prepareResourceHandlerSeparator(value));

        return generateUrl(assetParts);
    }

    /**
     * @param value the expression value
     * @return the expression value with "#" replaced by ":"
     */
    private String prepareResourceHandlerSeparator(String value)
    {
        if (value == null)
        {
            return null;
        }

        return value.replace("#", ":");
    }

    @Override
    public boolean valueNeeded()
    {
        return true;
    }
}
