package io.github.furti.spring.web.extended.io;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

public class LocalizedResourceLoaderImpl implements LocalizedResourceLoader
{
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private ResourceScanners scanners;

    // private ResourceLoader resourceLoader;

    @Override
    public Resource getResource(String resource, Locale locale)
    {
        if (resource == null || locale == null)
        {
            return null;
        }

        String[] pathAndFile = ResourceUtils.pathAndFile(resource);
        try
        {
            Map<String, Resource> resources = scanners.scanResources(pathAndFile[0], pathAndFile[1], false);

            if (resources == null)
            {
                return null;
            }

            List<String> localizedFiles = ResourceUtils.localizedResources(pathAndFile[1], locale);

            for (Entry<String, Resource> entry : resources.entrySet())
            {
                String fileName = ResourceUtils.pathAndFile(entry.getKey())[1];

                if (localizedFiles.contains(fileName))
                {
                    return entry.getValue();
                }
            }
        }
        catch (IOException e)
        {
            logger.error("Error scanning resources", e);
            return null;
        }

        return null;

        // List<String> resourceNames =
        // ResourceUtils.localizedResources(resource,
        // locale);
        //
        // if (resourceNames == null)
        // {
        // return null;
        // }
        //
        // for (String resourceLocation : resourceNames)
        // {
        // Resource r = resourceLoader.getResource(resourceLocation);
        //
        // if (r != null && r.exists())
        // {
        // return r;
        // }
        // }
        //
        // return null;
    }

    @Autowired
    public void setScanners(ResourceScanners scanners)
    {
        this.scanners = scanners;
    }

    // public void setResourceLoader(ResourceLoader resourceLoader)
    // {
    // this.resourceLoader = resourceLoader;
    // }

}
