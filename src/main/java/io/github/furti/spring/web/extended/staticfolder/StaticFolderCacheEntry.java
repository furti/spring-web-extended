/**
 * 
 */
package io.github.furti.spring.web.extended.staticfolder;

import static io.github.furti.spring.web.extended.util.SpringWebExtendedUtils.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
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
    private final ConcurrentHashMap<TemplateCacheKey, Template> templateCache =
        new ConcurrentHashMap<TemplateCacheKey, Template>(30);

    private final TemplateFactory templateFactory;
    private final TemplateContextFactory contextFactory;
    private final ResourceScanners scanners;
    private final String location;
    private final Charset charset;
    private final boolean reloadOnMissingResource;
    private final boolean cacheTemplates;

    public StaticFolderCacheEntry(ResourceScanners scanners, String location, Charset charset,
        boolean reloadOnMissingResource, boolean cacheTemplates, TemplateFactory templateFactory,
        TemplateContextFactory contextFactory)
    {
        super();
        this.scanners = scanners;
        this.location = location;
        this.charset = charset;
        this.reloadOnMissingResource = reloadOnMissingResource;
        this.templateFactory = templateFactory;
        this.contextFactory = contextFactory;
        this.cacheTemplates = cacheTemplates;
    }

    public void reload()
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

    public void refresh() throws IOException
    {
        for (Template template : templateCache.values())
        {
            try
            {
                template.refreshIfNeeded();
            }
            catch (IOException e)
            {
                throw new IOException(String.format("Error refreshing template %s", template), e);
            }
        }
    }

    public String renderResource(String file, HttpServletRequest request)
        throws ResourceNotFoundException, ResourceRenderException
    {
        Resource resource = files.get(file);

        if (resource == null && reloadOnMissingResource)
        {
            synchronized (lock)
            {
                resource = files.get(file);

                // If the resource is still null we can assume noone else has loaded the resource yet. So refresh the full folder.
                if (resource == null)
                {
                    reload();
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

    @Override
    public String toString()
    {
        return "StaticFolderCacheEntry [location=" + location + "]";
    }

    private String doRender(Resource resource, HttpServletRequest request) throws ResourceRenderException
    {
        try
        {
            TemplateContext context = contextFactory.createContext(request);
            Template template;

            if (cacheTemplates)
            {
                template = getTemplateFromCache(resource, context);

                if (template == null)
                {
                    template = templateFactory.createTemplate(resource, context, charset);
                    template.refreshIfNeeded();

                    templateCache.put(new TemplateCacheKey(resource, context), template);
                }
            }
            else
            {
                template = templateFactory.createTemplate(resource, context, charset);
                template.refreshIfNeeded();
            }

            return template.render();
        }
        catch (IOException e)
        {
            throw new ResourceRenderException(String.format("Error rendering resource %s", resource), e);
        }
    }

    private Template getTemplateFromCache(Resource resource, TemplateContext context)
    {
        for (Entry<TemplateCacheKey, Template> entry : templateCache.entrySet())
        {
            TemplateCacheKey key = entry.getKey();

            if (Objects.equals(key.context, context) && Objects.equals(key.resource, resource))
            {
                return entry.getValue();
            }
        }

        return null;
    }

    private static class TemplateCacheKey
    {
        private final Resource resource;
        private final TemplateContext context;

        TemplateCacheKey(Resource resource, TemplateContext context)
        {
            super();
            this.resource = resource;
            this.context = context;
        }
    }
}
