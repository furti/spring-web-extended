/**
 *
 */
package io.github.furti.spring.web.extended.util;

import jakarta.servlet.ServletContext;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import org.springframework.util.MimeType;

/**
 * @author Daniel Furtlehner
 */
public class MimeTypeHandler {

    private final Map<String, String> wellKnownMimeTypes = new HashMap<>();
    private final ServletContext servletContext;
    private final Set<MimeType> cacheableMimeTypes = new HashSet<>();
    private final MimeTypeCacheConfig defaultCacheConfig;
    private final Map<MimeType, MimeTypeCacheConfig> mimetypeCacheConfigurations;

    public MimeTypeHandler(
        ServletContext servletContext,
        Map<String, String> mimeTypes,
        Set<MimeType> cacheableMimeTypes,
        MimeTypeCacheConfig defaultCacheConfig,
        Map<MimeType, MimeTypeCacheConfig> mimetypeCacheConfigurations
    ) {
        this.servletContext = servletContext;

        this.wellKnownMimeTypes.putAll(mimeTypes);
        this.cacheableMimeTypes.addAll(cacheableMimeTypes);
        this.defaultCacheConfig = defaultCacheConfig;
        this.mimetypeCacheConfigurations = mimetypeCacheConfigurations;
    }

    public MimeType getMimeType(String file) {
        for (Entry<String, String> entry : wellKnownMimeTypes.entrySet()) {
            if (file.endsWith(entry.getKey())) {
                return MimeType.valueOf(entry.getValue());
            }
        }

        String mimeType = servletContext.getMimeType(file);

        if (mimeType != null) {
            return MimeType.valueOf(mimeType);
        }

        throw new IllegalArgumentException(String.format("No mimetype for %s found", file));
    }

    /**
     * @param file the file to check caching for
     * @return the value for the Cache-Control header or a empty optional, if the file should not be cached at all.
     */
    public Optional<String> getCacheConfig(String file) {
        MimeType mimeType = getMimeType(file);

        if (!cacheableMimeTypes.contains(mimeType)) {
            return Optional.empty();
        }

        MimeTypeCacheConfig cacheConfig = mimetypeCacheConfigurations.get(mimeType);

        if (cacheConfig == null) {
            cacheConfig = defaultCacheConfig;
        }

        return Optional.of(cacheConfig.cacheConfig());
    }
}
