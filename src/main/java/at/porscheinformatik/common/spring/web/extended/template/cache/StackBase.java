package at.porscheinformatik.common.spring.web.extended.template.cache;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import at.porscheinformatik.common.spring.web.extended.io.ResourceType;
import at.porscheinformatik.common.spring.web.extended.io.ResourceUtils;
import at.porscheinformatik.common.spring.web.extended.util.SpringWebExtendedUtils;

public abstract class StackBase extends AbstractTemplateCache
{
    private ResourceType resourceType;
    protected Logger logger = LoggerFactory.getLogger(getClass());

    public StackBase(ResourceType resourceType, String stackName,
        boolean noCaching)
    {
        super(stackName, noCaching);
        this.resourceType = resourceType;
        setupLastRefresh();
    }

    public void addResource(String name, StackEntry entry) throws IOException
    {
        if (entry.isScanLocation())
        {
            scanResources(entry.getLocation(), entry.isSkipProcessing());
        }
        else
        {
            addResource(name, entry.getLocation(), false,
                entry.isSkipProcessing());
            addResource(name, entry.getMinifiedLocation(), true,
                entry.isSkipProcessing());
        }
    }

    /**
     * Scan all resources in the location and add them to the stack
     * 
     * @param location
     * @throws IOException
     */
    private void scanResources(String location, boolean skipProcessing)
        throws IOException
    {
        Map<String, Resource> resources = scanners.scanResources(location);

        if (resources == null)
        {
            logger.warn("No resources found in location " + location);
            return;
        }

        for (Entry<String, Resource> entry : resources.entrySet())
        {
            addTemplate(entry.getKey().toLowerCase(), location,
                entry.getValue(),
                resourceType, false, skipProcessing);
        }
    }

    private void addResource(String name, String location,
        boolean optimizedResource, boolean skipProcessing)
        throws IOException
    {
        if (location == null)
        {
            return;
        }

        String[] prefixAndPath = SpringWebExtendedUtils
            .parseExpression(location);
        String[] pathAndFile = ResourceUtils.pathAndFile(prefixAndPath[1]);

        if (pathAndFile == null)
        {
            return;
        }

        String locationToUse = null;

        if (StringUtils.hasText(prefixAndPath[0]))
        {
            locationToUse = prefixAndPath[0] + ":";
        }
        else
        {
            locationToUse = "";
        }

        locationToUse += pathAndFile[0];

        Map<String, Resource> resources = scanners.scanResources(
            locationToUse, pathAndFile[1], false);

        /*
         * Spring does not log the message right if we only throw a
         * illegalargumentexception. So we log it manually here
         */
        if (resources == null)
        {
            String message = "No Resources for name " + name
                + " and location " + location + " found";

            IllegalArgumentException ex = new IllegalArgumentException(message);
            logger.error(message, ex);

            throw ex;
        }

        for (Entry<String, Resource> entry : resources.entrySet())
        {
            addTemplate(prepareName(name, entry.getKey()), location,
                entry.getValue(),
                resourceType, optimizedResource, skipProcessing);
        }
    }

    /**
     * Add the locale to the name
     * 
     * @param name
     * @param key
     * @return
     */
    private String prepareName(String styleName, String resourceName)
    {
        String[] styleNameAndEnding = ResourceUtils.getNameAndEnding(styleName);
        String[] resourceNameAndEnding = ResourceUtils
            .getNameAndEnding(resourceName);

        String locale = ResourceUtils
            .getLocaleFromName(resourceNameAndEnding[0]);

        StringBuilder name = new StringBuilder(styleNameAndEnding[0]);

        if (locale != null)
        {
            name.append("_").append(locale);
        }

        if (styleNameAndEnding[1] != null)
        {
            name.append(styleNameAndEnding[1]);
        }

        return name.toString();
    }
}
