/**
 * 
 */
package io.github.furti.spring.web.extended.staticfolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.MimeType;

import io.github.furti.spring.web.extended.ApplicationInfo;
import io.github.furti.spring.web.extended.MessageRegistry;
import io.github.furti.spring.web.extended.SpringWebExtendedConfigurer;
import io.github.furti.spring.web.extended.StaticFolderRegistry;
import io.github.furti.spring.web.extended.expression.ExpressionHandlerRegistry;
import io.github.furti.spring.web.extended.io.ResourceScanner;
import io.github.furti.spring.web.extended.template.DefaultContentEscapeHandlerRegistry;
import io.github.furti.spring.web.extended.template.escape.HtmlEscapeHandler;
import io.github.furti.spring.web.extended.template.escape.JavascriptEscapeHandler;
import io.github.furti.spring.web.extended.template.simple.ContentEscapeHandlerRegistry;

/**
 * @author Daniel Furtlehner
 */
@Configuration
public class StaticFolderConfigurerConfiguration
{
    @Autowired
    private Environment env;

    private final DelegatingSpringWebExtendedConfigurer configurer = new DelegatingSpringWebExtendedConfigurer();
    private DefaultStaticFolderRegistry staticFolderRegistry;
    private Map<String, String> mimeTypes;
    private DefaultApplicationInfo applicationInfo;
    private MessageRegistry messageRegistry;
    private DefaultContentEscapeHandlerRegistry contentEscapeHandlerRegistry;

    @Autowired(required = false)
    public void setConfigurers(List<SpringWebExtendedConfigurer> configurers)
    {
        configurer.addConfigurers(configurers);
    }

    public ApplicationInfo getApplicationInfo()
    {
        if (applicationInfo == null)
        {
            applicationInfo = new DefaultApplicationInfo();

            applicationInfo.productionMode(env.acceptsProfiles("optimizeresources", "prod", "production"));

            configurer.configureApplication(applicationInfo);
        }

        return applicationInfo;
    }

    public StaticFolderRegistry getStaticFolderRegistry()
    {
        if (staticFolderRegistry == null)
        {
            staticFolderRegistry = new DefaultStaticFolderRegistry();

            boolean productionMode = getApplicationInfo().isProductionMode();

            staticFolderRegistry.reloadOnMissingResource(!productionMode);
            staticFolderRegistry.templateRefreshInterval(productionMode ? 60 * 10 : 0);

            configurer.configureStaticFolders(staticFolderRegistry);
        }

        return staticFolderRegistry;
    }

    public ContentEscapeHandlerRegistry getContentExceptHandlerRegistry()
    {
        if (contentEscapeHandlerRegistry == null)
        {
            contentEscapeHandlerRegistry = new DefaultContentEscapeHandlerRegistry();

            JavascriptEscapeHandler javascriptHandler = new JavascriptEscapeHandler();

            contentEscapeHandlerRegistry.registerHandler(MimeType.valueOf("text/html"), new HtmlEscapeHandler());
            contentEscapeHandlerRegistry.registerHandler(MimeType.valueOf("text/javascript"), javascriptHandler);
            contentEscapeHandlerRegistry.registerHandler(MimeType.valueOf("application/javascript"), javascriptHandler);

            configurer.configureContentEscapeHandlers(contentEscapeHandlerRegistry);
        }

        return contentEscapeHandlerRegistry;
    }

    public Map<String, String> getMimeTypes()
    {
        if (mimeTypes == null)
        {
            mimeTypes = new HashMap<>();

            // Add some basic mime types
            mimeTypes.put(".js.map", "application/json");

            configurer.configureMimeTypes(mimeTypes);
        }

        return mimeTypes;
    }

    public MessageRegistry getMessageRegistry()
    {
        if (messageRegistry == null)
        {
            messageRegistry = new DefaultMessageRegistry();

            configurer.configureMessages(messageRegistry);
        }

        return messageRegistry;
    }

    public void configureResourceScanners(Map<String, ResourceScanner> scanners)
    {
        configurer.configureResourceScanners(scanners);
    }

    public void configureExpressionHandlers(ExpressionHandlerRegistry registry)
    {
        configurer.configureExpressionHandlers(registry);
    }
}
