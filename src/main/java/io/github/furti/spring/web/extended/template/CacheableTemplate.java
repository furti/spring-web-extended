/**
 * 
 */
package io.github.furti.spring.web.extended.template;

import java.io.IOException;

import org.springframework.core.io.Resource;

/**
 * @author Daniel Furtlehner
 */
public abstract class CacheableTemplate implements Template
{
    private final Object lock = new Object();

    protected final Resource resource;
    private String content = "";
    private long lastModified;

    public CacheableTemplate(Resource resource)
    {
        super();
        this.resource = resource;
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

}
