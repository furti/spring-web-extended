/**
 *
 */
package io.github.furti.spring.web.extended.template.legacy;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import io.github.furti.spring.web.extended.http.LinkCreator;
import io.github.furti.spring.web.extended.io.ResourceUtils;
import io.github.furti.spring.web.extended.util.AssetUtils;
import ro.isdc.wro.model.resource.Resource;
import ro.isdc.wro.model.resource.processor.ResourcePreProcessor;

/**
 * @author Daniel Furtlehner
 */
public class SourceMappingUrlProcessor implements ResourcePreProcessor
{
    private static final Pattern PATTERN = Pattern.compile("^//# sourceMappingURL=(.*)$", Pattern.MULTILINE);

    private final LinkCreator linkCreator;

    public SourceMappingUrlProcessor(LinkCreator linkCreator)
    {
        super();
        this.linkCreator = linkCreator;
    }

    @Override
    public void process(Resource resource, Reader reader, Writer writer) throws IOException
    {
        try
        {
            final String content = IOUtils.toString(reader);

            Matcher matcher = PATTERN.matcher(content);

            String result = content;

            //If we have a sourceMappingUrl --> change it
            while (matcher.find())
            {
                //We found a url
                if (matcher.groupCount() >= 1)
                {
                    String sourceMappingUrl = prepareSourceMappingUrl(resource.getUri(), matcher.group(1));

                    result = matcher.replaceFirst("//# sourceMappingURL=" + sourceMappingUrl);
                }
            }

            writer.write(result);
        }
        finally
        {
            reader.close();
            writer.close();
        }
    }

    private String prepareSourceMappingUrl(String jsUri, String path)
    {
        String normalized = ResourceUtils.normalize(jsUri, path);
        String[] assetParts = AssetUtils.createAssetParts(normalized);

        return linkCreator.createLink(assetParts);
    }
}
