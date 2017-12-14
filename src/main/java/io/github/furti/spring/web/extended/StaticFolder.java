/**
 * 
 */
package io.github.furti.spring.web.extended;

import java.nio.charset.Charset;
import java.util.Objects;

/**
 * @author Daniel Furtlehner
 */
public class StaticFolder
{

    private final String basePath;
    private final String location;
    private final Charset charset;

    public StaticFolder(String basePath, String location, Charset charset)
    {
        super();

        this.basePath = Objects.requireNonNull(basePath, "basePath must not be null");
        this.location = Objects.requireNonNull(location, "location must not be null");
        this.charset = Objects.requireNonNull(charset, "charset must not be null");

        if (!this.location.endsWith("/"))
        {
            throw new IllegalArgumentException("The location must end with /");
        }

        if (!basePath.startsWith("/"))
        {
            throw new IllegalArgumentException("The basePath must start with /");
        }
    }

    public String getBasePath()
    {
        return basePath;
    }

    public String getLocation()
    {
        return location;
    }

    public Charset getCharset()
    {
        return charset;
    }

    @Override
    public String toString()
    {
        return "StaticFolder [basePath=" + basePath + ", location=" + location + ", charset=" + charset + "]";
    }
}
