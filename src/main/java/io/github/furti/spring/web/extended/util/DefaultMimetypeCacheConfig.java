/**
 * 
 */
package io.github.furti.spring.web.extended.util;

import static io.github.furti.spring.web.extended.util.DefaultMimetypeCacheConfig.CacheMode.*;

/**
 * @author Daniel Furtlehner
 */
public class DefaultMimetypeCacheConfig implements MimeTypeCacheConfig
{
    // Cache for a day. Then the client must revalidate
    private static final long DEFAULT_CACHE_TIME_IN_SECONDS = 24 * 60 * 60;

    public enum CacheMode
    {
        PUBLIC("public"),
        PRIVATE("private");

        private final String mode;

        private CacheMode(String mode)
        {
            this.mode = mode;
        }

        public String getMode()
        {
            return mode;
        }
    }

    private final CacheMode mode;
    private final long cacheTimeInSeconds;
    private final boolean revalidate;

    public DefaultMimetypeCacheConfig()
    {
        this(PUBLIC, DEFAULT_CACHE_TIME_IN_SECONDS, true);
    }

    public DefaultMimetypeCacheConfig(CacheMode mode, long cacheTimeInSeconds, boolean revalidate)
    {
        super();

        this.mode = mode;
        this.cacheTimeInSeconds = cacheTimeInSeconds;
        this.revalidate = revalidate;
    }

    public DefaultMimetypeCacheConfig mode(CacheMode mode)
    {
        return new DefaultMimetypeCacheConfig(mode, cacheTimeInSeconds, revalidate);
    }

    public DefaultMimetypeCacheConfig maxAge(long maxAgeInSeconds)
    {
        return new DefaultMimetypeCacheConfig(mode, maxAgeInSeconds, revalidate);
    }

    public DefaultMimetypeCacheConfig mustRevalidate(boolean revalidate)
    {
        return new DefaultMimetypeCacheConfig(mode, cacheTimeInSeconds, revalidate);
    }

    @Override
    public String cacheConfig()
    {
        if (revalidate)
        {
            return String.format("%s, max-age=%s, must-revalidate", mode.getMode(), cacheTimeInSeconds);
        }
        else
        {
            return String.format("%s, max-age=%s", mode.getMode(), cacheTimeInSeconds);
        }
    }
}
