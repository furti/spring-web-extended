package at.porscheinformatik.common.spring.web.extended.util;

public final class PathUtils
{

    public static String join(String path, String... subPaths)
    {
        if (subPaths.length == 0)
        {
            return path;
        }

        StringBuilder fullPath = new StringBuilder(removeTrailingSlashes(path));

        for (int i = 0; i < subPaths.length; i++)
        {
            String subPath = subPaths[i];

            if (i < subPaths.length - 1)
            {
                subPath = removeTrailingSlashes(subPath);
            }

            fullPath.append("/").append(removeLeadingSlashes(subPath));
        }

        return fullPath.toString();
    }

    public static String removeLeadingSlashes(String path)
    {
        while (path.startsWith("/"))
        {
            path = path.substring(1);
        }

        return path;
    }

    public static final String removeTrailingSlashes(String path)
    {
        while (path.endsWith("/"))
        {
            path = path.substring(0, path.length() - 1);
        }

        return path;

    }

    public static String ensureLeadingSlash(String path)
    {
        if (!path.startsWith("/"))
        {
            return "/" + path;
        }

        return path;
    }

}
