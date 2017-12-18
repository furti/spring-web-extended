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

import io.github.furti.spring.web.extended.StaticFolder;
import io.github.furti.spring.web.extended.StaticFolderRegistry;
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
    private final Map<String, StaticFolderCacheEntry> entries = new HashMap<>();
    private final MimeTypeHandler mimeTypeHandler;
    private final ResourceScanners scanners;
    private final StaticFolderRegistry registry;
    private final TemplateFactory templateFactory;
    private final TemplateContextFactory contextFactory;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    public StaticFolderCache(StaticFolderRegistry registry, ResourceScanners scanners, MimeTypeHandler mimeTypeHandler,
        TemplateFactory templateFactory, TemplateContextFactory contextFactory)
    {
        this.registry = registry;
        this.scanners = scanners;
        this.mimeTypeHandler = mimeTypeHandler;
        this.templateFactory = templateFactory;
        this.contextFactory = contextFactory;
    }

    @PostConstruct
    public void initialize()
    {
        logger.info("Setting up static folders: " + registry.toString());

        for (StaticFolder staticFolder : registry.getFolders())
        {
            StaticFolderCacheEntry entry = new StaticFolderCacheEntry(scanners, staticFolder.getLocation(),
                staticFolder.getCharset(), registry.isReloadOnMissingResource(),
                registry.getTemplateRefreshInterval() != 0, templateFactory, contextFactory);

            entry.reload();

            entries.put(staticFolder.getBasePath(), entry);
        }
    }

    public void refreshFolders()
    {
        for (StaticFolderCacheEntry entry : entries.values())
        {
            try
            {
                entry.refresh();
            }
            catch (IOException e)
            {
                logger.error(String.format("Error refreshing template for folder%s", entry), e);
            }
        }
    }

    public ResponseEntity<String> render(HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();

        String path = normalizePath(requestURI);

        RenderEntry entry = findEntryForPath(path);

        if (entry == null)
        {
            throw new ResourceNotFoundException(String.format("%s could not be found in any folder", requestURI));
        }

        String content;
        try
        {
            content = entry.render(request);
        }
        catch (ResourceNotFoundException e)
        {
            throw new ResourceNotFoundException(
                String.format("%s could not be found in folder %s", entry.file, entry.basePath));
        }
        catch (ResourceRenderException e)
        {
            throw new RuntimeException(String.format("Error rendering file %s in folder %s for url %s", entry.file,
                entry.basePath, requestURI), e);
        }

        return new ResponseEntity<String>(content, buildHeaders(entry), HttpStatus.OK);
    }

    private MultiValueMap<String, String> buildHeaders(RenderEntry entry)
    {
        //TODO: handle caching of values. We can set the lastmodified header and spring handles caching for us

        HttpHeaders headers = new HttpHeaders();

        MimeType mimeType = this.mimeTypeHandler.getMimeType(entry.file);

        headers.setContentType(new MediaType(mimeType.getType(), mimeType.getSubtype(), entry.entry.getCharset()));

        return headers;
    }

    private RenderEntry findEntryForPath(String path)
    {
        for (Entry<String, StaticFolderCacheEntry> entry : entries.entrySet())
        {
            if (path.startsWith(entry.getKey()))
            {
                String file = path.substring(entry.getKey().length());

                //Fallback to index if the folder was requested
                if (StringUtils.isEmpty(file))
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

    private String normalizePath(String requestURI)
    {
        //TODO: we have to remove common things like the locale and application version when present.

        if (!requestURI.startsWith("/"))
        {
            requestURI = "/" + requestURI;
        }

        return requestURI;
    }

    private static class RenderEntry
    {
        private StaticFolderCacheEntry entry;
        private String basePath;
        private String file;

        RenderEntry(StaticFolderCacheEntry entry, String basePath, String file)
        {
            super();
            this.entry = entry;
            this.basePath = basePath;
            this.file = file;
        }

        public String render(HttpServletRequest request) throws ResourceNotFoundException, ResourceRenderException
        {
            return entry.renderResource(file, request);
        }
    }
}
