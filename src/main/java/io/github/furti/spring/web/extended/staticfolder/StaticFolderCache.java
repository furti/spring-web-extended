/**
 * 
 */
package io.github.furti.spring.web.extended.staticfolder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import org.springframework.util.MultiValueMap;

import io.github.furti.spring.web.extended.ApplicationInfo;
import io.github.furti.spring.web.extended.StaticFolder;
import io.github.furti.spring.web.extended.StaticFolderRegistry;
import io.github.furti.spring.web.extended.compression.CompressionManager;
import io.github.furti.spring.web.extended.io.ResourceScanners;
import io.github.furti.spring.web.extended.template.TemplateContextFactory;
import io.github.furti.spring.web.extended.template.TemplateFactory;
import io.github.furti.spring.web.extended.util.MimeTypeHandler;
import io.github.furti.spring.web.extended.util.ResourceNotFoundException;

/**
 * @author Daniel Furtlehner
 */
public class StaticFolderCache
{
    // Cache for a day. Then the client must revalidate
    private static final long CACHE_TIME_IN_SECONDS = 24 * 60 * 60;

    private final Map<String, StaticFolderCacheEntry> entries = new HashMap<>();
    private final StaticFolderRegistry registry;
    private final ResourceScanners scanners;
    private final MimeTypeHandler mimeTypeHandler;
    private final TemplateFactory templateFactory;
    private final TemplateContextFactory contextFactory;
    private final ResourceTypeRegistry resourceTypeRegistry;
    private final ApplicationInfo appInfo;
    private final CompressionManager compressionManager;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    public StaticFolderCache(StaticFolderRegistry registry, ResourceScanners scanners, MimeTypeHandler mimeTypeHandler,
        TemplateFactory templateFactory, TemplateContextFactory contextFactory,
        ResourceTypeRegistry resourceTypeRegistry, ApplicationInfo appInfo, CompressionManager compressionManager)
    {
        super();
        this.registry = registry;
        this.scanners = scanners;
        this.mimeTypeHandler = mimeTypeHandler;
        this.templateFactory = templateFactory;
        this.contextFactory = contextFactory;
        this.resourceTypeRegistry = resourceTypeRegistry;
        this.appInfo = appInfo;
        this.compressionManager = compressionManager;
    }

    @PostConstruct
    public void initialize()
    {
        logger.info("Setting up static folders: " + registry.toString());

        for (StaticFolder staticFolder : registry.getFolders())
        {
            StaticFolderCacheEntry entry = new StaticFolderCacheEntry(scanners, staticFolder.getLocation(),
                staticFolder.getIndexFallbacks(), staticFolder.getCharset(), registry.isReloadOnMissingResource(),
                registry.getResourceRefreshInterval() != 0, templateFactory, contextFactory, resourceTypeRegistry,
                mimeTypeHandler, compressionManager);

            entry.reload();

            entries.put(staticFolder.getBasePath(), entry);
        }
    }

    public void refreshFolders(boolean force)
    {
        for (StaticFolderCacheEntry entry : entries.values())
        {
            try
            {
                entry.refresh(force);
            }
            catch (IOException e)
            {
                logger.error(String.format("Error refreshing template for folder%s", entry), e);
            }
        }
    }

    public ResponseEntity<byte[]> render(HttpServletRequest request)
    {
        RenderEntry entry = findEntryForRequest(request);

        if (entry == null)
        {
            throw new ResourceNotFoundException(
                String.format("%s could not be found in any folder", request.getRequestURI()));
        }

        StaticFolderRenderResponse renderResponse;
        try
        {
            renderResponse = entry.render(request);
        }
        catch (ResourceNotFoundException e)
        {
            throw new ResourceNotFoundException(
                String.format("%s could not be found in folder %s", entry.file, entry.basePath));
        }
        catch (ResourceRenderException e)
        {
            throw new RuntimeException(String
                .format("Error rendering file %s in folder %s for url %s", entry.file, entry.basePath,
                    request.getRequestURI()),
                e);
        }

        return new ResponseEntity<byte[]>(renderResponse.getContent(),
            buildHeaders(entry, request, renderResponse.getHeaders()), HttpStatus.OK);
    }

    private MultiValueMap<String, String> buildHeaders(RenderEntry entry, HttpServletRequest request,
        Map<String, String> additionalHeaders)
    {
        HttpHeaders headers = new HttpHeaders();

        MimeType mimeType = this.mimeTypeHandler.getMimeType(entry.file);

        headers.setContentType(new MediaType(mimeType.getType(), mimeType.getSubtype(), entry.entry.getCharset()));

        headers.setLastModified(entry.entry.getLastModified(entry.file, request));

        if (appInfo.isProductionMode() && this.mimeTypeHandler.shouldBeCached(entry.file))
        {
            headers.setCacheControl("public, max-age=" + CACHE_TIME_IN_SECONDS + ", must-revalidate");
            headers.setPragma("cache");
        }
        else
        {
            headers.setCacheControl("no-store, max-age=0, must-revalidate");
            headers.setPragma("no-cache");
        }

        if (additionalHeaders != null)
        {
            additionalHeaders
                .entrySet()
                .forEach(additionalHeader -> headers.add(additionalHeader.getKey(), additionalHeader.getValue()));
        }

        return headers;
    }

    RenderEntry findEntryForRequest(HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();

        String path = normalizePath(request.getContextPath(), requestURI);

        for (Entry<String, StaticFolderCacheEntry> entry : entries.entrySet())
        {
            if (path.startsWith(entry.getKey()))
            {
                String file = path.substring(entry.getKey().length());

                //Fallback to index if the folder was requested
                if (StringUtils.isEmpty(file) || "/".equals(file) || entry.getValue().isIndexFallback(file))
                {
                    file = "index.html";
                }

                if (file.startsWith("/"))
                {
                    file = file.substring(1);
                }

                return new RenderEntry(entry.getValue(), entry.getKey(), file);
            }
        }

        return null;
    }

    private String normalizePath(String contextPath, String requestURI)
    {
        //Remove the context path from the uri. All uris are relative to the context path.
        if (contextPath.length() > 0)
        {
            requestURI = requestURI.substring(contextPath.length());
        }

        if (!requestURI.startsWith("/"))
        {
            requestURI = "/" + requestURI;
        }

        return requestURI;
    }

    static class RenderEntry
    {
        StaticFolderCacheEntry entry;
        String basePath;
        String file;

        RenderEntry(StaticFolderCacheEntry entry, String basePath, String file)
        {
            super();
            this.entry = entry;
            this.basePath = basePath;
            this.file = file;
        }

        public StaticFolderRenderResponse render(HttpServletRequest request)
            throws ResourceNotFoundException, ResourceRenderException
        {
            return entry.renderResource(file, request);
        }
    }
}
