/**
 * 
 */
package io.github.furti.spring.web.extended.template.chunk;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.core.io.Resource;

import com.x5.template.Chunk;
import com.x5.template.Theme;

import io.github.furti.spring.web.extended.template.CacheableTemplate;

/**
 * @author Daniel Furtlehner
 */
public class ChunkTemplate extends CacheableTemplate
{
    private final Map<String, String> stringsToReplace;
    private final Theme theme;

    public ChunkTemplate(Resource resource, Theme theme, Charset charset, Map<String, String> stringsToReplace)
    {
        super(resource, charset);

        this.theme = theme;
        this.stringsToReplace = stringsToReplace;
    }

    @Override
    public String doRender() throws IOException
    {
        Chunk chunk = buildChunk();

        return chunk.toString();
    }

    private Chunk buildChunk() throws IOException
    {
        Chunk chunk = theme.makeChunk();

        String templateContent = prepareTemplate();

        chunk.append(templateContent);

        return chunk;
    }

    private String prepareTemplate() throws IOException
    {
        String templateContent = loadTemplate();

        for (Entry<String, String> entry : stringsToReplace.entrySet())
        {
            //We have to replace some character combinations in the template. Otherwise chunk assumes some magic that should not happen.
            templateContent = templateContent.replace(entry.getKey(), entry.getValue());
        }

        return templateContent;
    }

    @Override
    public String toString()
    {
        return "ChunkTemplate [resource=" + resource + "]";
    }
}
