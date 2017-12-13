package io.github.furti.spring.web.extended.template.cache;

import org.springframework.util.StringUtils;

public final class CacheUtils
{

    private CacheUtils()
    {

    }

    public static String buildPath(String configName, String path)
    {
        StringBuilder builder = new StringBuilder();

        if (StringUtils.hasText(configName))
        {
            builder.append(configName).append("/");
        }

        if (StringUtils.hasText(path))
        {
            if (path.contains("."))
            {
                int index = path.lastIndexOf(".");

                builder.append(path.substring(0, index));
            }
            else
            {
                builder.append(path);
            }
        }

        return builder.toString();
    }
}
