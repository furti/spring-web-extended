/**
 * 
 */
package io.github.furti.spring.web.extended.template;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;

/**
 * @author Daniel Furtlehner
 */
public abstract class CacheableTemplate implements Template
{
    private final Object lock = new Object();

    protected final Resource resource;
    protected final Charset charset;
    private String content = "";
    private long lastModified;

    public CacheableTemplate(Resource resource, Charset charset)
    {
        super();
        this.resource = resource;
        this.charset = charset;
    }

    @Override
    public String render() throws IOException
    {
        return content;
    }

    @Override
    public void refreshIfNeeded() throws IOException
    {
        synchronized (lock)
        {
            if (lastModified == 0 || lastModified != resource.lastModified())
            {
                content = doRender();

                lastModified = resource.lastModified();
            }
        }
    }

    protected abstract String doRender() throws IOException;

    protected String loadTemplate() throws IOException
    {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), charset))
        {
            return IOUtils.toString(reader);
        }
    }
}
