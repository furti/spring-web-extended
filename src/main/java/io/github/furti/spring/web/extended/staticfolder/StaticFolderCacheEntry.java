/**
 * 
 */
package io.github.furti.spring.web.extended.staticfolder;

import static io.github.furti.spring.web.extended.util.SpringWebExtendedUtils.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;

import io.github.furti.spring.web.extended.io.ResourceScanners;
import io.github.furti.spring.web.extended.util.ResourceNotFoundException;

/**
 * @author Daniel Furtlehner
 */
public class StaticFolderCacheEntry
{
    private static final String FILE_PATTERN = "**/*";

    private final Object lock = new Object();
    private final ResourceScanners scanners;
    private final String location;

    private final ConcurrentHashMap<String, Resource> files = new ConcurrentHashMap<>(30);

    public StaticFolderCacheEntry(ResourceScanners scanners, String location)
    {
        this.scanners = scanners;
        this.location = location;
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

    public String renderResource(String file) throws ResourceNotFoundException, ResourceRenderException
    {
        Resource resource = files.get(file);

        if (resource == null)
        {
            synchronized (lock)
            {
                resource = files.get(file);
                /*
                 * TODO: we should make it configurable if we should refresh the entry on missing resources.
                 * In a production environment this should not happen.
                 * In a development environment this is a nice feature to refresh on a missing resource.
                 */

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

        //TODO: add template engine
        return doRender(resource);
    }

    private String doRender(Resource resource) throws ResourceRenderException
    {
        try
        {
            InputStream input = resource.getInputStream();

            // TODO: make charset configurabe per folder
            return IOUtils.toString(input, Charset.forName("UTF-8"));
        }
        catch (IOException e)
        {
            throw new ResourceRenderException(e);
        }
    }
}
