/**
 * 
 */
package io.github.furti.spring.web.extended.staticfolder;

import static java.util.Objects.*;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.springframework.util.MimeType;

import io.github.furti.spring.web.extended.util.DefaultMimetypeCacheConfig;
import io.github.furti.spring.web.extended.util.MimeTypeCacheConfig;

/**
 * @author Daniel Furtlehner
 */
public class MimeTypeCacheRegistry
{
    private MimeTypeCacheConfig defaultCacheConfig = new DefaultMimetypeCacheConfig();
    private Map<MimeType, MimeTypeCacheConfig> mimeTypeCacheConfigs = new HashMap<>();

    public MimeTypeCacheConfig getDefaultCacheConfig()
    {
        return defaultCacheConfig;
    }

    public MimeTypeCacheRegistry defaultCacheConfig(@Nonnull MimeTypeCacheConfig defaultCacheConfig)
    {
        this.defaultCacheConfig = requireNonNull(defaultCacheConfig, "The default cache config must not be null");

        return this;
    }

    public Map<MimeType, MimeTypeCacheConfig> getMimeTypeCacheConfigs()
    {
        return mimeTypeCacheConfigs;
    }

    public MimeTypeCacheRegistry mimeTypeCacheConfig(MimeType mimeType, @Nullable MimeTypeCacheConfig cacheConfig)
    {
        mimeTypeCacheConfigs.put(mimeType, cacheConfig);

        return this;
    }
}
