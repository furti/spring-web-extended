package io.github.furti.spring.web.extended.util;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * @author Daniel Furtlehner
 *
 */
public final class AssetUtils
{

    private AssetUtils()
    {

    }

    public static String[] createAssetParts(String assetLocation)
    {
        String[] split = SpringWebExtendedUtils.parseExpression(assetLocation);
        Assert.isTrue(split.length == 2, "Invalid asset " + assetLocation);

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

        return urlParts;
    }
}
