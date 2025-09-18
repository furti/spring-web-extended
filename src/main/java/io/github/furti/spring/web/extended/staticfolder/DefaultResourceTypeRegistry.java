/**
 *
 */
package io.github.furti.spring.web.extended.staticfolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.MimeType;

/**
 * @author Daniel Furtlehner
 */
public class DefaultResourceTypeRegistry implements ResourceTypeRegistry {

    //TODO: add tests
    private final Map<Pattern, ResourceType> resourceTypesByNamePattern = new HashMap<>();
    private final Map<Pattern, ResourceType> resourceTypesByMimeTypePattern = new HashMap<>();

    @Override
    public ResourceTypeRegistry resourceTypeByName(String namePattern, ResourceType resourceType) {
        Assert.notNull(namePattern, "The name pattern must not be null");
        Assert.notNull(resourceType, "The ResourceType must not be null");

        resourceTypesByNamePattern.put(Pattern.compile(namePattern), resourceType);

        return this;
    }

    @Override
    public ResourceTypeRegistry resourceTypeByMimeType(String mimeTypePattern, ResourceType resourceType) {
        Assert.notNull(mimeTypePattern, "The name pattern must not be null");
        Assert.notNull(resourceType, "The ResourceType must not be null");

        resourceTypesByMimeTypePattern.put(Pattern.compile(mimeTypePattern), resourceType);

        return this;
    }

    @Override
    public ResourceType getResourceType(Resource resource, MimeType mimeType) {
        String resourceName = resource.getFilename();

        // Check the names first. A matching name should override a matching mimetype
        ResourceType type = findTypeByPattern(resourceName, resourceTypesByNamePattern);

        if (type == null) {
            type = findTypeByPattern(mimeType.toString(), resourceTypesByMimeTypePattern);
        }

        if (type == null) {
            type = ResourceType.BINARY;
        }

        return type;
    }

    private ResourceType findTypeByPattern(String value, Map<Pattern, ResourceType> typesByPattern) {
        for (Entry<Pattern, ResourceType> entry : typesByPattern.entrySet()) {
            if (entry.getKey().matcher(value).matches()) {
                return entry.getValue();
            }
        }

        return null;
    }
}
