package io.github.furti.spring.web.extended.io;

import io.github.furti.spring.web.extended.util.SpringWebExtendedUtils;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

public class ResourceScanners {

    private final Map<String, ResourceScanner> scanners;

    public ResourceScanners(Map<String, ResourceScanner> scanners) {
        super();
        this.scanners = scanners;
    }

    /**
     * @param resourcePath
     * @param basePath
     * @return the found resources or an empty map when no resources where found.
     * @throws IOException
     */
    public Map<String, Resource> scanResources(String resourcePath, String basePath) throws IOException {
        String[] split = SpringWebExtendedUtils.parseExpression(resourcePath);

        Assert.isTrue(split.length == 2, "Invalid expression " + resourcePath);
        Assert.isTrue(scanners.containsKey(split[0]), "Unknown expression prefix " + split[0]);

        Map<String, Resource> foundResources = scanners.get(split[0]).scanResources(split[1], basePath);

        if (foundResources == null) {
            foundResources = Collections.emptyMap();
        }

        return foundResources;
    }

    /**
     * @param location
     * @param file
     * @param scanSubDirectories
     * @return the found resources or an empty map when no resources where found.
     * @throws IOException
     */
    public Map<String, Resource> scanResources(String location, String file, boolean scanSubDirectories)
        throws IOException {
        String[] split = SpringWebExtendedUtils.parseExpression(location);

        Assert.isTrue(split.length == 2, "Invalid expression " + location);
        Assert.isTrue(scanners.containsKey(split[0]), "Unknown expression prefix " + split[0]);

        Map<String, Resource> foundResources = scanners.get(split[0]).scanResources(split[1], file, scanSubDirectories);

        if (foundResources == null) {
            foundResources = Collections.emptyMap();
        }

        return foundResources;
    }
}
