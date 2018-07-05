/**
 * 
 */
package io.github.furti.spring.web.extended.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletContext;

import org.springframework.util.MimeType;

/**
 * @author Daniel Furtlehner
 */
public class MimeTypeHandler
{

    private final Map<String, String> wellKnownMimeTypes = new HashMap<>();
    private final ServletContext servletContext;
    private final Set<MimeType> cacheableMimeTypes = new HashSet<>();

    public MimeTypeHandler(ServletContext servletContext, Map<String, String> mimeTypes,
        Set<MimeType> cacheableMimeTypes)
    {
        this.servletContext = servletContext;

        wellKnownMimeTypes.putAll(mimeTypes);
        this.cacheableMimeTypes.addAll(cacheableMimeTypes);
    }

    public MimeType getMimeType(String file)
    {

        for (Entry<String, String> entry : wellKnownMimeTypes.entrySet())
        {
            if (file.endsWith(entry.getKey()))
            {
                return MimeType.valueOf(entry.getValue());
            }
        }

        String mimeType = servletContext.getMimeType(file);

        if (mimeType != null)
        {
            return MimeType.valueOf(mimeType);
        }

        throw new IllegalArgumentException(String.format("No mimetype for %s found", file));
    }

    public boolean shouldBeCached(String file)
    {
        MimeType mimeType = getMimeType(file);

        return cacheableMimeTypes.contains(mimeType);
    }
}
