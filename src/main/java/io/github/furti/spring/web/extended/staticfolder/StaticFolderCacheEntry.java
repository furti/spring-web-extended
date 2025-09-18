/**
 *
 */
package io.github.furti.spring.web.extended.staticfolder;

import static io.github.furti.spring.web.extended.util.SpringWebExtendedUtils.*;

import io.github.furti.spring.web.extended.compression.CompressionManager;
import io.github.furti.spring.web.extended.compression.CompressionType;
import io.github.furti.spring.web.extended.io.ResourceScanners;
import io.github.furti.spring.web.extended.template.Template;
import io.github.furti.spring.web.extended.template.TemplateContext;
import io.github.furti.spring.web.extended.template.TemplateContextFactory;
import io.github.furti.spring.web.extended.template.TemplateFactory;
import io.github.furti.spring.web.extended.util.MimeTypeHandler;
import io.github.furti.spring.web.extended.util.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.MimeType;

/**
 * @author Daniel Furtlehner
 */
public class StaticFolderCacheEntry {

    private static final String FILE_PATTERN = "**/*";

    private final Object lock = new Object();
    private final ConcurrentHashMap<String, Resource> files = new ConcurrentHashMap<>(30);
    private final ConcurrentHashMap<TemplateCacheKey, StaticFolderTemplateEntry> templateCache = new ConcurrentHashMap<
        TemplateCacheKey,
        StaticFolderTemplateEntry
    >(30);
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
    private final CompressionManager compressionManager;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public StaticFolderCacheEntry(
        ResourceScanners scanners,
        String location,
        Set<String> indexFallbacks,
        Charset charset,
        boolean reloadOnMissingResource,
        boolean cacheResources,
        TemplateFactory templateFactory,
        TemplateContextFactory contextFactory,
        ResourceTypeRegistry resourceTypeRegistry,
        MimeTypeHandler mimeTypeHandler,
        CompressionManager compressionManager
    ) {
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
        this.compressionManager = compressionManager;
    }

    public void reload() {
        try {
            Map<String, Resource> resources = scanners.scanResources(
                this.location + FILE_PATTERN,
                stripPrefix(this.location)
            );

            files.putAll(resources);
        } catch (IOException e) {
            throw new RuntimeException(
                String.format("An error occured while refreshing the resources for %s", this.location),
                e
            );
        }
    }

    public void refresh(boolean force) throws IOException {
        for (StaticFolderTemplateEntry template : templateCache.values()) {
            try {
                template.refresh(force);
            } catch (IOException e) {
                throw new IOException(String.format("Error refreshing template %s", template), e);
            }
        }

        for (Entry<Resource, byte[]> entry : resourceCache.entrySet()) {
            entry.setValue(getContentFromInputStream(entry.getKey()));
        }
    }

    public StaticFolderRenderResponse renderResource(String file, HttpServletRequest request)
        throws ResourceNotFoundException, ResourceRenderException {
        Resource resource = findResource(file);

        MimeType mimeType = mimeTypeHandler.getMimeType(resource.getFilename());
        ResourceType resourceType = resourceTypeRegistry.getResourceType(resource, mimeType);

        switch (resourceType) {
            case TEMPLATE:
                CompressionType requestedCompressionType = compressionManager.getRequestedCompressionType(
                    request,
                    mimeType
                );

                return doRenderTemplate(resource, request, requestedCompressionType);
            case BINARY:
                return doRenderBinary(resource, request);
            default:
                throw new ResourceRenderException("ResourceType " + resourceType + " is not implemented yet.");
        }
    }

    Resource findResource(String file) {
        Resource resource = files.get(file);

        if (resource == null && reloadOnMissingResource) {
            synchronized (lock) {
                resource = files.get(file);

                // If the resource is still null we can assume noone else has loaded the resource yet. So refresh the full folder.
                if (resource == null) {
                    reload();
                }
            }

            resource = files.get(file);
        }

        if (resource == null || !resource.exists()) {
            throw new ResourceNotFoundException(file);
        }
        return resource;
    }

    public Charset getCharset() {
        return charset;
    }

    public boolean isIndexFallback(String file) {
        for (String fallback : indexFallbacks) {
            if (pathMatcher.match(fallback, file)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param file the file to geht the lastmodified for
     * @param request the request
     * @return time when the resource was last modiefied in epoch millis
     */
    public long getLastModified(String file, HttpServletRequest request) {
        try {
            Resource resource = findResource(file);

            ResourceType resourceType = resourceTypeRegistry.getResourceType(
                resource,
                mimeTypeHandler.getMimeType(resource.getFilename())
            );

            switch (resourceType) {
                case TEMPLATE:
                    StaticFolderTemplateEntry template = buildTemplate(resource, request);

                    return template.getLastRefreshed();
                case BINARY:
                    return resource.lastModified();
                default:
                    throw new IllegalArgumentException("Unknown resource type " + resourceType);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error getting lastmodified for file " + file, e);
        }
    }

    @Override
    public String toString() {
        return "StaticFolderCacheEntry [location=" + location + "]";
    }

    StaticFolderRenderResponse doRenderTemplate(
        Resource resource,
        HttpServletRequest request,
        CompressionType compressionType
    ) throws ResourceRenderException {
        try {
            StaticFolderTemplateEntry template = buildTemplate(resource, request);

            return template.render(compressionType, charset);
        } catch (IOException e) {
            throw new ResourceRenderException(String.format("Error rendering resource %s", resource), e);
        }
    }

    protected StaticFolderTemplateEntry buildTemplate(Resource resource, HttpServletRequest request)
        throws IOException {
        TemplateContext context = contextFactory.createContext(request, resource);
        StaticFolderTemplateEntry entry;

        if (cacheResources) {
            entry = getTemplateFromCache(resource, context);

            if (entry == null) {
                Template template = templateFactory.createTemplate(resource, context, charset);
                template.refreshIfNeeded();

                entry = new StaticFolderTemplateEntry(template, compressionManager);

                templateCache.put(new TemplateCacheKey(resource, context), entry);
            }
        } else {
            Template template = templateFactory.createTemplate(resource, context, charset);
            template.refreshIfNeeded();

            entry = new StaticFolderTemplateEntry(template, compressionManager);
        }

        return entry;
    }

    private StaticFolderRenderResponse doRenderBinary(Resource resource, HttpServletRequest request)
        throws ResourceRenderException {
        try {
            if (cacheResources) {
                byte[] content = resourceCache.get(resource);

                if (content == null) {
                    content = getContentFromInputStream(resource);
                }

                return new StaticFolderRenderResponse(content);
            } else {
                return new StaticFolderRenderResponse(getContentFromInputStream(resource));
            }
        } catch (IOException e) {
            throw new ResourceRenderException(String.format("Error streaming resource %s", resource), e);
        }
    }

    private byte[] getContentFromInputStream(Resource resource) throws IOException {
        try (InputStream input = resource.getInputStream()) {
            return IOUtils.toByteArray(input);
        }
    }

    private StaticFolderTemplateEntry getTemplateFromCache(Resource resource, TemplateContext context) {
        for (Entry<TemplateCacheKey, StaticFolderTemplateEntry> entry : templateCache.entrySet()) {
            TemplateCacheKey key = entry.getKey();

            if (Objects.equals(key.context, context) && Objects.equals(key.resource, resource)) {
                return entry.getValue();
            }
        }

        return null;
    }

    private static class TemplateCacheKey {

        private final Resource resource;
        private final TemplateContext context;

        TemplateCacheKey(Resource resource, TemplateContext context) {
            super();
            this.resource = resource;
            this.context = context;
        }
    }
}
