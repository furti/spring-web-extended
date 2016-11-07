package at.porscheinformatik.common.spring.web.extended.asset;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import at.porscheinformatik.common.spring.web.extended.config.ApplicationConfiguration;
import at.porscheinformatik.common.spring.web.extended.io.LocalizedResourceLoader;
import at.porscheinformatik.common.spring.web.extended.util.RequestUtils;

/**
 * Controller that sends static resources to the client.
 *
 * @author Daniel Furtlehner
 *
 */
@Controller
public class AssetController extends ResourceHttpRequestHandler
{

    private static final Pattern PATH_PATTERN = Pattern.compile("^.*asset/(.*)");

    private LocalizedResourceLoader resourceLoader;
    private ApplicationConfiguration appConfig;

    /**
     * Handles every URL that cotains "asset" and streams the requested Resource to the client.
     * 
     * @param request - The Request
     * @param response - The Response
     * @throws IOException - If an exception occurs while streaming the resource
     * @throws ServletException
     */
    @RequestMapping(value = "/**/asset/**", method = RequestMethod.GET)
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

        return resourceLoader.getResource(resourcePath, LocaleContextHolder.getLocale());
    }

    @Autowired
    public void setAppConfig(ApplicationConfiguration appConfig)
    {
        this.appConfig = appConfig;
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
}
