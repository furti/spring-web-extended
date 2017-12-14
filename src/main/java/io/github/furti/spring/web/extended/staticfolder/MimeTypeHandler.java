/**
 * 
 */
package io.github.furti.spring.web.extended.staticfolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;

/**
 * @author Daniel Furtlehner
 */
@Service
public class MimeTypeHandler
{

    private final Map<String, String> wellKnownMimeTypes = new HashMap<>();
    private final ServletContext servletContext;

    @Autowired
    public MimeTypeHandler(ServletContext servletContext)
    {
        this.servletContext = servletContext;

        //TODO: make them configurable via the configurer
        wellKnownMimeTypes.put(".js.map", "application/json");
    }

    public MimeType getMimeType(String file)
    {
        String mimeType = servletContext.getMimeType(file);

        if (mimeType != null)
        {
            return MimeType.valueOf(mimeType);
        }

        for (Entry<String, String> entry : wellKnownMimeTypes.entrySet())
        {
            if (file.endsWith(entry.getKey()))
            {
                return MimeType.valueOf(entry.getValue());
            }
        }

        throw new IllegalArgumentException(String.format("No mimetype for %s found", file));
    }
}
