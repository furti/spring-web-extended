package io.github.furti.spring.web.extended.io;

import java.io.IOException;
import java.util.Map;

import org.springframework.core.io.Resource;

public abstract class AbstractResourceScanner implements ResourceScanner
{

    @Override
    public Map<String, Resource> scanResources(String pattern, String basePath) throws IOException
    {
        if (basePath == null)
        {
            basePath = constructBasePathFromPattern(pattern);
        }

        return doScanResources(pattern, basePath);
    }

    protected abstract Map<String, Resource> doScanResources(String pattern, String basePath) throws IOException;

    private String constructBasePathFromPattern(String pattern)
    {
        int firstIndexOfAsterisk = pattern.indexOf("*");

        String basePath = firstIndexOfAsterisk < 0 ? pattern : pattern.substring(0, firstIndexOfAsterisk);

        if (basePath.endsWith("/"))
        {
            return basePath.substring(0, basePath.length() - 1);
        }

        return basePath;
    }
}
