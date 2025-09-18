/**
 *
 */
package io.github.furti.spring.web.extended.util;

import jakarta.annotation.Nonnull;

/**
 * @author Daniel Furtlehner
 */
public interface MimeTypeCacheConfig {
    /**
     * @return the cache value that can be passed to the Cache-Control HTTP header.
     */
    @Nonnull
    String cacheConfig();
}
