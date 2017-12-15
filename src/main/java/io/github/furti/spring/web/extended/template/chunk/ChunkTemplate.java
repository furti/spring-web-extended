/**
 * 
 */
package io.github.furti.spring.web.extended.template.chunk;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;

import com.x5.template.Chunk;
import com.x5.template.Theme;

import io.github.furti.spring.web.extended.template.Template;

/**
 * @author Daniel Furtlehner
 */
public class ChunkTemplate implements Template
{
    private final Map<String, String> stringsToReplace = new HashMap<>();

    private final Resource resource;
    private final Theme theme;
    private final Charset charset;

    public ChunkTemplate(Resource resource, Theme theme, Charset charset)
    {
        this.resource = resource;
        this.theme = theme;
        this.charset = charset;

        //TODO: maybe we should use our own template stuff? Lot of stuff to replace. Maybe this breaks something.
        stringsToReplace.put("{/", "{ /");
        stringsToReplace.put("{!", "{ !");
        stringsToReplace.put("{_", "{ _");
        stringsToReplace.put("{$", "{ $");
        stringsToReplace.put("_[", "_ [");
    }

    @Override
    public String render() throws IOException
    {
        Chunk chunk = buildChunk();

        return chunk.toString();
    }

    private Chunk buildChunk() throws IOException
    {
        Chunk chunk = theme.makeChunk();

        // TODO: add caching of template content.
        String templateContent = prepareTemplate();

        chunk.append(templateContent);

        return chunk;
    }

    private String prepareTemplate() throws IOException
    {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), charset))
        {
            String templateContent = IOUtils.toString(reader);

            for (Entry<String, String> entry : stringsToReplace.entrySet())
            {
                //We have to replace some character combinations in the template. Otherwise chunk assumes some magic that should not happen.
                templateContent = templateContent.replace(entry.getKey(), entry.getValue());
            }

            return templateContent;
        }
    }
}
