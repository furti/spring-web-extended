/**
 * 
 */
package io.github.furti.spring.web.extended.staticfolder;

import static io.github.furti.spring.web.extended.util.SpringWebExtendedUtils.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;

import io.github.furti.spring.web.extended.io.ResourceScanners;
import io.github.furti.spring.web.extended.template.Template;
import io.github.furti.spring.web.extended.template.TemplateContext;
import io.github.furti.spring.web.extended.template.TemplateContextFactory;
import io.github.furti.spring.web.extended.template.TemplateFactory;
import io.github.furti.spring.web.extended.util.ResourceNotFoundException;

/**
 * @author Daniel Furtlehner
 */
public class StaticFolderCacheEntry
{
    private static final String FILE_PATTERN = "**/*";

    private final Object lock = new Object();
    private final ConcurrentHashMap<String, Resource> files = new ConcurrentHashMap<>(30);

    private final TemplateFactory templateFactory;
    private final TemplateContextFactory contextFactory;
    private final ResourceScanners scanners;
    private final String location;
    private final Charset charset;
    private final boolean refreshOnMissingResource;

    public StaticFolderCacheEntry(ResourceScanners scanners, String location, Charset charset,
        boolean refreshOnMissingResource, TemplateFactory templateFactory, TemplateContextFactory contextFactory)
    {
        super();
        this.scanners = scanners;
        this.location = location;
        this.charset = charset;
        this.refreshOnMissingResource = refreshOnMissingResource;
        this.templateFactory = templateFactory;
        this.contextFactory = contextFactory;
    }

    public void refresh()
    {
        try
        {
            Map<String, Resource> resources =
                scanners.scanResources(this.location + FILE_PATTERN, stripPrefix(this.location));

            files.putAll(resources);
        }
        catch (IOException e)
        {
            throw new RuntimeException(
                String.format("An error occured while refreshing the resources for %s", this.location), e);
        }
    }

    public String renderResource(String file, HttpServletRequest request)
        throws ResourceNotFoundException, ResourceRenderException
    {
        Resource resource = files.get(file);

        if (resource == null && refreshOnMissingResource)
        {
            synchronized (lock)
            {
                resource = files.get(file);

                // If the resource is still null we can assume noone else has loaded the resource yet. So refresh the full folder.
                if (resource == null)
                {
                    refresh();
                }
            }

            resource = files.get(file);
        }

        if (resource == null || !resource.exists())
        {
            throw new ResourceNotFoundException(file);
        }

        return doRender(resource, request);
    }

    public Charset getCharset()
    {
        return charset;
    }

    private String doRender(Resource resource, HttpServletRequest request) throws ResourceRenderException
    {
        try
        {
            //TODO: add caching and refresh of templates
            TemplateContext context = contextFactory.createContext(request);
            Template template = templateFactory.createTemplate(resource, context, charset);

            return template.render();
        }
        catch (IOException e)
        {
            throw new ResourceRenderException(String.format("Error rendering resource %s", resource), e);
        }
    }
}
