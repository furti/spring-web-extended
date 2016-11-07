package at.porscheinformatik.common.spring.web.extended.template;

import at.porscheinformatik.common.spring.web.extended.http.LinkCreator;
import at.porscheinformatik.common.spring.web.extended.io.ResourceUtils;
import at.porscheinformatik.common.spring.web.extended.util.AssetUtils;
import ro.isdc.wro.model.resource.processor.impl.css.AbstractCssUrlRewritingProcessor;

/**
 * @author Daniel Furtlehner
 *
 */
public class CssAssetUrlProcessor extends AbstractCssUrlRewritingProcessor
{
    private final LinkCreator linkCreator;

    public CssAssetUrlProcessor(LinkCreator linkCreator)
    {
        super();
        this.linkCreator = linkCreator;
    }

    @Override
    protected String replaceImageUrl(String cssUri, String imageUrl)
    {
        if (!imageUrl.startsWith(".."))
        {
            return imageUrl;
        }

        String normalized = ResourceUtils.normalize(cssUri, imageUrl);
        String[] assetParts = AssetUtils.createAssetParts(normalized);

        return linkCreator.createLink(assetParts);
    }

}
