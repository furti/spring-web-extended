/**
 *
 */
package io.github.furti.spring.web.extended.staticfolder;

import org.springframework.core.io.Resource;
import org.springframework.util.MimeType;

/**
 * @author Daniel Furtlehner
 */
public interface ResourceTypeRegistry {
    /**
     * @param resource the resource
     * @param mimeType the resources mime type
     * @return the {@link ResourceType} for the given resource
     */
    ResourceType getResourceType(Resource resource, MimeType mimeType);

    /**
     * @param namePattern if the filename matches to this regex, the given {@link ResourceType} will be used.
     * @param resourceType the {@link ResourceType} to use
     * @return the registry for a fluent api
     */
    ResourceTypeRegistry resourceTypeByName(String namePattern, ResourceType resourceType);

    /**
     * @param mimeTypePattern if the resources mimetype matches to this regex, the given {@link ResourceType} will be
     *            used.
     * @param resourceType the {@link ResourceType} to use
     * @return the registry for a fluent api
     */
    ResourceTypeRegistry resourceTypeByMimeType(String mimeTypePattern, ResourceType resourceType);
}
