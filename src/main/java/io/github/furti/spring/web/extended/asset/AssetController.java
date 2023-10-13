package io.github.furti.spring.web.extended.asset;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import io.github.furti.spring.web.extended.config.ApplicationConfiguration;
import io.github.furti.spring.web.extended.io.LocalizedResourceLoader;
import io.github.furti.spring.web.extended.io.ResourceUtils;
import io.github.furti.spring.web.extended.util.RequestUtils;

/**
 * Controller that sends static resources to the client.
 *
 * @author Daniel Furtlehner
 */
@Controller
public class AssetController extends ResourceHttpRequestHandler
{

    private static final Pattern PATH_PATTERN = Pattern.compile("^.*asset/(.*)");

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final List<String> loggedPaths = new ArrayList<String>(50);

    private LocalizedResourceLoader resourceLoader;
    private ApplicationConfiguration appConfig;
    private AssetFolderWhitelist whitelist;

    /**
     * Handles every URL that cotains "asset" and streams the requested Resource to the client.
     * 
     * @param request - The Request
     * @param response - The Response
     * @throws IOException - If an exception occurs while streaming the resource
     * @throws ServletException
     */
    @RequestMapping(value = {"/asset/**", "/*/*/asset/**", "/*/*/*/asset/**"}, method = RequestMethod.GET)
    public void handleAsset(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        handleRequest(request, response);
    }

    private String buildResourceFromPath(String path)
    {
        if (path == null)
        {
            return null;
        }

        int index = path.indexOf("/");
        Assert.isTrue(index > -1, "Could not get Asset for path " + path);

        String prefix = path.substring(0, index);
        String resource = path.substring(index + 1);

        StringBuilder builder = new StringBuilder();

        if (!prefix.equals("context"))
        {
            builder.append(prefix).append(":");
        }

        builder.append(resource);

        return builder.toString();
    }

    @Autowired
    public void setResourceLoader(LocalizedResourceLoader resourceLoader)
    {
        this.resourceLoader = resourceLoader;
    }

    @Override
    protected Resource getResource(HttpServletRequest request)
    {
        String path = RequestUtils.getPathFromRegex(request, PATH_PATTERN);
        String resourcePath = buildResourceFromPath(path);
        String folder = ResourceUtils.pathAndFile(resourcePath)[0];

        if (!isAllowed(folder))
        {
            if (shouldWarn(folder))
            {
                logger.warn("Path {} is not configured to serve assets.", folder);
            }

            return null;
        }

        return resourceLoader.getResource(resourcePath, LocaleContextHolder.getLocale());
    }

    private boolean shouldWarn(String path)
    {
        /*
         * We only log the first 50 wrong entries. If we have more than 50,
         * it looks like an attack and we don't want an attacker to flood our logs
         */
        if (!logger.isWarnEnabled() || loggedPaths.size() >= 50)
        {
            return false;
        }

        if (loggedPaths.contains(path))
        {
            return false;
        }

        loggedPaths.add(path);

        return true;
    }

    private boolean isAllowed(String path)
    {
        return whitelist.contains(path);
    }

    @Autowired
    public void setAppConfig(ApplicationConfiguration appConfig)
    {
        this.appConfig = appConfig;
    }

    @Autowired
    public void setWhitelist(AssetFolderWhitelist whitelist)
    {
        this.whitelist = whitelist;
    }

    @PostConstruct
    public void init()
    {
        if (appConfig.isOptimizeResources())
        {
            setCacheSeconds(365 * 24 * 60 * 60);
        }
        else
        {
            setCacheSeconds(0);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        // Initialize the handler with a dummy resource
        this
            .setLocations(Arrays
                .asList(new ByteArrayResource(
                    "Should never be served. Otherwise something is wrong with the Asset Controller.".getBytes())));

        super.afterPropertiesSet();
    }
}
