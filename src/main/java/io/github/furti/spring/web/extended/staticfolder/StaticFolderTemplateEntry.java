/**
 * 
 */
package io.github.furti.spring.web.extended.staticfolder;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentHashMap;

import io.github.furti.spring.web.extended.compression.CompressionType;
import io.github.furti.spring.web.extended.template.Template;

/**
 * @author Daniel Furtlehner
 */
public class StaticFolderTemplateEntry
{
    private final ConcurrentHashMap<CompressionType, byte[]> compressionCache = new ConcurrentHashMap<>(1);
    private final Template template;

    public StaticFolderTemplateEntry(Template template)
    {
        super();

        this.template = template;
    }

    /**
     * @param force true to force refreshing even when nothing changed.
     * @throws IOException when something goes wrong while refreshing the template
     */
    public void refresh(boolean force) throws IOException
    {
        if (force)
        {
            template.forceRefresh();
            compressionCache.clear();
        }
        else
        {
            if (template.refreshIfNeeded())
            {
                compressionCache.clear();
            }
        }
    }

    public long getLastRefreshed()
    {
        return template.getLastRefreshed();
    }

    public StaticFolderRenderResponse render(CompressionType compressionType, Charset charset) throws IOException
    {
        switch (compressionType)
        {
            case NO_COMPRESSION:
                return new StaticFolderRenderResponse(template.render().getBytes(charset));
            default:
                return renderCompressed(compressionType, charset);
        }
    }

    private StaticFolderRenderResponse renderCompressed(CompressionType compressionType, Charset charset)
        throws IOException
    {
        byte[] compressed = compressionCache.get(compressionType);

        if (compressed == null)
        {
            String content = template.render();

            // TODO: compress the content with the compression manager

            throw new UnsupportedOperationException("Implement");
        }

        StaticFolderRenderResponse response = new StaticFolderRenderResponse(compressed);
        response.setContentEncoding(compressionType.getValue());

        return response;
    }
}
