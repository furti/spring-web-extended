package at.porscheinformatik.common.spring.web.extended.io;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.ServletContextAware;

/**
 * Scans the webapp directory for resources.
 * 
 * @author Daniel Furtlehner
 */
public class ContextResourceScanner implements ServletContextAware,
    ResourceScanner
{

    private ServletContext context;

    public void setServletContext(ServletContext context)
    {
        this.context = context;
    }

    @Override
    public Map<String, Resource> scanResources(String path) throws IOException
    {
        Set<String> templatePaths = new HashSet<>();
        addTemplates(preparePath(path), true, null, templatePaths);

        return createResources(path, templatePaths);
    }

    @Override
    public Map<String, Resource> scanResources(String path, String file,
        boolean scanSubDirectories) throws IOException
    {
        Set<String> templatePaths = new HashSet<>();
        final String[] nameAndEnding = ResourceUtils.getNameAndEnding(file);

        addTemplates(preparePath(path), scanSubDirectories, new FileMatcher()
        {

            @Override
            public boolean matches(String path)
            {
                String filename = ResourceUtils.pathAndFile(path)[1];
                int baselength = nameAndEnding[0].length();

                if (!filename.startsWith(nameAndEnding[0]))
                {
                    return false;
                }

                if (nameAndEnding[1] != null)
                {
                    baselength += nameAndEnding[1].length();

                    if (!filename.endsWith(nameAndEnding[1]))
                    {
                        return false;
                    }
                }

                /*
                 * If the file has more characters than the basename the
                 * character following the filename must be a _ to be an
                 * locale. Else we might have a minimized version of the
                 * same resource. So ignore it
                 */
                if (filename.length() > baselength)
                {
                    return filename.charAt(nameAndEnding[0].length()) == '_';
                }

                return true;
            }

        }, templatePaths);

        return createResources(path, templatePaths);
    }

    private void addTemplates(String path, boolean scanSubDirectories, FileMatcher matcher, Set<String> templates)
    {
        Set<String> paths = context.getResourcePaths(path);

        if (paths == null || paths.isEmpty())
        {
            return;
        }

        for (String resourcePath : paths)
        {
            //We have a directory and we must scan it
            if (resourcePath.endsWith("/"))
            {
                if (scanSubDirectories)
                {
                    addTemplates(resourcePath, scanSubDirectories, matcher, templates);
                }
            }
            else
            {
                if (matcher == null || matcher.matches(resourcePath))
                {
                    templates.add(resourcePath);
                }
            }
        }
    }

    /**
     * @param path
     * @return
     */
    private String preparePath(String path)
    {
        if (!path.startsWith("/"))
        {
            return "/" + path;
        }

        return path;
    }

    private Map<String, Resource> createResources(String path, Set<String> templateFiles) throws MalformedURLException
    {
        if (CollectionUtils.isEmpty(templateFiles))
        {
            return null;
        }

        Map<String, Resource> resources = new HashMap<String, Resource>();

        for (String templateFile : templateFiles)
        {
            String relative = templateFile.substring(path.length() + 1).replace("\\", "/");

            if (relative.startsWith("/"))
            {
                relative = relative.substring(1);
            }

            URL resourceURL = context.getResource(templateFile);
            
            resources.put(relative, new UrlResource(resourceURL));
        }

        return resources;
    }

    private static interface FileMatcher
    {

        boolean matches(String path);
    }
}
