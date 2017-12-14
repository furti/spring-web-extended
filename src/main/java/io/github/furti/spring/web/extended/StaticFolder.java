/**
 * 
 */
package io.github.furti.spring.web.extended;

import java.util.Objects;

/**
 * @author Daniel Furtlehner
 */
public class StaticFolder
{

    private final String basePath;
    private final String location;

    public StaticFolder(String basePath, String location)
    {
        super();

        this.basePath = Objects.requireNonNull(basePath, "basePath must not be null");
        this.location = Objects.requireNonNull(location, "location must not be null");

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

    @Override
    public String toString()
    {
        return "StaticFolder [basePath=" + basePath + ", location=" + location + "]";
    }
}
