/**
 * 
 */
package io.github.furti.spring.web.extended.staticfolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.MediaType;
import org.springframework.util.MimeType;

import io.github.furti.spring.web.extended.ApplicationInfo;
import io.github.furti.spring.web.extended.MessageRegistry;
import io.github.furti.spring.web.extended.SpringWebExtendedConfigurer;
import io.github.furti.spring.web.extended.StaticFolderRegistry;
import io.github.furti.spring.web.extended.compression.CompressionManager;
import io.github.furti.spring.web.extended.compression.DefaultCompressionManager;
import io.github.furti.spring.web.extended.expression.ExpressionHandlerRegistry;
import io.github.furti.spring.web.extended.io.ResourceScanner;
import io.github.furti.spring.web.extended.locale.LocaleSource;
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
    private Set<MimeType> cacheableMimeTypes;
    private DefaultApplicationInfo applicationInfo;
    private MessageRegistry messageRegistry;
    private DefaultContentEscapeHandlerRegistry contentEscapeHandlerRegistry;
    private ResourceTypeRegistry resourceTypeRegistry;
    private List<LocaleSource> localeSources;
    private CompressionManager compressionManager;
    private MimeTypeCacheRegistry mimeTypeCacheRegistry;

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

            applicationInfo.productionMode(env.acceptsProfiles(Profiles.of("optimizeresources", "prod", "production")));

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
            staticFolderRegistry.resourceRefreshInterval(productionMode ? 60 * 10 : 0);

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

    public CompressionManager getCompressionManager()
    {
        if (compressionManager == null)
        {
            List<MimeType> supportedMimeTypes = new ArrayList<>();

            supportedMimeTypes.add(MediaType.parseMediaType("text/*"));
            supportedMimeTypes.add(MediaType.APPLICATION_JSON);
            supportedMimeTypes.add(MediaType.APPLICATION_JSON_UTF8);
            supportedMimeTypes.add(MediaType.parseMediaType("application/javascript"));

            configurer.configureCompressableMimeTypes(supportedMimeTypes);

            compressionManager = new DefaultCompressionManager(supportedMimeTypes);
        }

        return compressionManager;
    }

    public Map<String, String> getMimeTypes()
    {
        if (mimeTypes == null)
        {
            mimeTypes = new HashMap<>();

            // Add some basic mime types
            mimeTypes.put(".js.map", "application/json");
            mimeTypes.put(".ico", "image/x-icon");

            configurer.configureMimeTypes(mimeTypes);
        }

        return mimeTypes;
    }

    public Set<MimeType> getCachableMimeTypes()
    {
        if (cacheableMimeTypes == null)
        {
            cacheableMimeTypes = new HashSet<>();

            // Cache some resources by default
            cacheableMimeTypes.add(MimeType.valueOf("application/javascript"));
            cacheableMimeTypes.add(MimeType.valueOf("text/css"));

            configurer.configureCacheableMimeTypes(cacheableMimeTypes);
        }

        return cacheableMimeTypes;
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

    public ResourceTypeRegistry getResourceTypeRegistry()
    {
        if (resourceTypeRegistry == null)
        {
            resourceTypeRegistry = new DefaultResourceTypeRegistry();

            resourceTypeRegistry.resourceTypeByMimeType("text/.*", ResourceType.TEMPLATE);
            resourceTypeRegistry.resourceTypeByMimeType("application/javascript", ResourceType.TEMPLATE);

            configurer.configureResourceTypes(resourceTypeRegistry);
        }

        return resourceTypeRegistry;
    }

    public MimeTypeCacheRegistry getMimetypCacheRegistry()
    {
        if (mimeTypeCacheRegistry == null)
        {
            mimeTypeCacheRegistry = new MimeTypeCacheRegistry();

            configurer.configureMimeTypeCaching(mimeTypeCacheRegistry);
        }

        return mimeTypeCacheRegistry;
    }

    public List<LocaleSource> getLocaleSources()
    {
        if (localeSources == null)
        {
            localeSources = new ArrayList<>();
            // No default locale sources for now. Simply use the request locale.

            configurer.configureLocaleSources(localeSources);
        }

        return localeSources;
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
