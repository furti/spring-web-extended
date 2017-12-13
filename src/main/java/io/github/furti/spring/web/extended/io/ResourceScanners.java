package io.github.furti.spring.web.extended.io;

import java.io.IOException;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import io.github.furti.spring.web.extended.util.SpringWebExtendedUtils;

public class ResourceScanners
{

    private final Map<String, ResourceScanner> scanners;

    public ResourceScanners(Map<String, ResourceScanner> scanners)
    {
        super();
        this.scanners = scanners;
    }

    public Map<String, Resource> scanResources(String resourcePath, String basePath) throws IOException
    {
        String[] split = SpringWebExtendedUtils.parseExpression(resourcePath);

        Assert.isTrue(split.length == 2, "Invalid expression " + resourcePath);
        Assert.isTrue(scanners.containsKey(split[0]), "Unknown expression prefix " + split[0]);

        return scanners.get(split[0]).scanResources(split[1], basePath);
    }

    public Map<String, Resource> scanResources(String location, String file, boolean scanSubDirectories)
        throws IOException
    {
        String[] split = SpringWebExtendedUtils.parseExpression(location);

        Assert.isTrue(split.length == 2, "Invalid expression " + location);
        Assert.isTrue(scanners.containsKey(split[0]), "Unknown expression prefix " + split[0]);

        return scanners.get(split[0]).scanResources(split[1], file, scanSubDirectories);
    }
}
