/**
 * 
 */
package io.github.furti.spring.web.extended.staticfolder;

import static io.github.furti.spring.web.extended.util.SpringWebExtendedUtils.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;

import io.github.furti.spring.web.extended.io.ResourceScanners;
import io.github.furti.spring.web.extended.template.Template;
import io.github.furti.spring.web.extended.template.TemplateContext;
import io.github.furti.spring.web.extended.template.TemplateContextFactory;
import io.github.furti.spring.web.extended.template.TemplateFactory;
import io.github.furti.spring.web.extended.util.MimeTypeHandler;
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
    private final ConcurrentHashMap<Resource, byte[]> resourceCache = new ConcurrentHashMap<Resource, byte[]>(30);

    private final ResourceScanners scanners;
    private final String location;
    private final Set<String> indexFallbacks;
    private final Charset charset;
    private final boolean reloadOnMissingResource;
    private final boolean cacheResources;
    private final TemplateFactory templateFactory;
    private final TemplateContextFactory contextFactory;
    private final ResourceTypeRegistry resourceTypeRegistry;
    private final MimeTypeHandler mimeTypeHandler;

    public StaticFolderCacheEntry(ResourceScanners scanners, String location, Set<String> indexFallbacks,
        Charset charset, boolean reloadOnMissingResource, boolean cacheResources, TemplateFactory templateFactory,
        TemplateContextFactory contextFactory, ResourceTypeRegistry resourceTypeRegistry,
        MimeTypeHandler mimeTypeHandler)
    {
        super();
        this.scanners = scanners;
        this.location = location;
        this.indexFallbacks = indexFallbacks;
        this.charset = charset;
        this.reloadOnMissingResource = reloadOnMissingResource;
        this.cacheResources = cacheResources;
        this.templateFactory = templateFactory;
        this.contextFactory = contextFactory;
        this.resourceTypeRegistry = resourceTypeRegistry;
        this.mimeTypeHandler = mimeTypeHandler;
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

        for (Entry<Resource, byte[]> entry : resourceCache.entrySet())
        {
            entry.setValue(getContentFromInputStream(entry.getKey()));
        }
    }

    public byte[] renderResource(String file, HttpServletRequest request)
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

        ResourceType resourceType =
            resourceTypeRegistry.getResourceType(resource, mimeTypeHandler.getMimeType(resource.getFilename()));

        switch (resourceType)
        {
            case TEMPLATE:
                return doRenderTemplate(resource, request);
            case BINARY:
                return doRenderBinary(resource, request);
            default:
                throw new ResourceRenderException("ResourceType " + resourceType + " is not implemented yet.");
        }
    }

    public Charset getCharset()
    {
        return charset;
    }

    public boolean isIndexFallback(String file)
    {
        return indexFallbacks.contains(file);
    }

    @Override
    public String toString()
    {
        return "StaticFolderCacheEntry [location=" + location + "]";
    }

    private byte[] doRenderTemplate(Resource resource, HttpServletRequest request) throws ResourceRenderException
    {
        try
        {
            TemplateContext context = contextFactory.createContext(request, resource);
            Template template;

            if (cacheResources)
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

            String content = template.render();

            return content.getBytes(charset);
        }
        catch (IOException e)
        {
            throw new ResourceRenderException(String.format("Error rendering resource %s", resource), e);
        }
    }

    private byte[] doRenderBinary(Resource resource, HttpServletRequest request) throws ResourceRenderException
    {
        try
        {
            if (cacheResources)
            {
                byte[] content = resourceCache.get(resource);

                if (content == null)
                {
                    content = getContentFromInputStream(resource);
                }

                return content;
            }
            else
            {
                return getContentFromInputStream(resource);
            }
        }
        catch (IOException e)
        {
            throw new ResourceRenderException(String.format("Error streaming resource %s", resource), e);
        }
    }

    private byte[] getContentFromInputStream(Resource resource) throws IOException
    {
        try (InputStream input = resource.getInputStream())
        {
            return IOUtils.toByteArray(input);
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
