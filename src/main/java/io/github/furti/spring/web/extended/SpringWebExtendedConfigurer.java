/**
 * 
 */
package io.github.furti.spring.web.extended;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.context.i18n.LocaleContext;
import org.springframework.util.MimeType;

import io.github.furti.spring.web.extended.expression.ExpressionHandlerRegistry;
import io.github.furti.spring.web.extended.io.ResourceScanner;
import io.github.furti.spring.web.extended.locale.LocaleSource;
import io.github.furti.spring.web.extended.staticfolder.MimeTypeCacheRegistry;
import io.github.furti.spring.web.extended.staticfolder.ResourceTypeRegistry;
import io.github.furti.spring.web.extended.staticfolder.StaticFolderCache;
import io.github.furti.spring.web.extended.staticfolder.StaticFoldersController;
import io.github.furti.spring.web.extended.template.DefaultContentEscapeHandlerRegistry;

/**
 * @author Daniel Furtlehner
 */
public interface SpringWebExtendedConfigurer
{

    /**
     * @param info the application info
     */
    default void configureApplication(ApplicationInfo info)
    {
        // Sublcasses override this for custom configuration
    };

    /**
     * Configure static folders to be served by the {@link StaticFoldersController}.
     * 
     * @param registry the registry that holds all the static folders
     */
    default void configureStaticFolders(StaticFolderRegistry registry)
    {
        // Sublcasses override this for custom configuration
    };

    /**
     * You can add or remove scanners to be used for scanning static folders here. A scanner has a prefix that allows it
     * to be referenced in resource locations. e.g. to scan a folder on the classpath you can use a resource string like
     * so "classpath:some/path/to/the/folder/"
     * 
     * @param scanners the map of scanners. Key is the prefix of the scanner.
     */
    default void configureResourceScanners(Map<String, ResourceScanner> scanners)
    {
        // Sublcasses override this for custom configuration
    };

    /**
     * Allows you to configure some well knonwn mime types in you application. If a mime type is not found in this list,
     * the servlet context will be asked for a mime type. So you can use this map to override standard mime types or add
     * mime types that the servlet context does not know.
     * <p>
     * Key is the file ending with a leading dot. Value is the mime type for this file ending.
     * </p>
     * 
     * @param mimeTypes available mime types
     */
    default void configureMimeTypes(Map<String, String> mimeTypes)
    {
        // Sublcasses override this for custom configuration
    };

    /**
     * Allm mime types in this list will have caching headers added to the response when the application runs in
     * production mode.
     * 
     * @param cacheableMimeTypes list of mime types that should be cached
     */
    default void configureCacheableMimeTypes(Set<MimeType> cacheableMimeTypes)
    {
        // Sublcasses override this for custom configuration
    }

    /**
     * Add or remove expression handlers for the application.
     * 
     * @param registry the expression handler registry
     */
    default void configureExpressionHandlers(ExpressionHandlerRegistry registry)
    {
        // Sublcasses override this for custom configuration
    };

    /**
     * @param messageRegistry the registry to hold the configured messages
     */
    default void configureMessages(MessageRegistry messageRegistry)
    {
        // Sublcasses override this for custom configuration
    };

    /**
     * Configures content escape handlers that are used by the template engine to escape expressions for the given mime
     * type of the template.
     * 
     * @param registry the registry to register escape handlers.
     */
    default void configureContentEscapeHandlers(DefaultContentEscapeHandlerRegistry registry)
    {
        // Sublcasses override this for custom configuration
    };

    /**
     * Configures resource types used by the {@link StaticFolderCache} to handle resources.
     * 
     * @param registry the registry
     */
    default void configureResourceTypes(ResourceTypeRegistry registry)
    {
        // Sublcasses override this for custom configuration
    };

    /**
     * Add some locale sources to use for the application. The locale sources are asked in the order they where
     * registered. The first locale that is returned will be used. To use the locale set by the locale source you can
     * simply inject the {@link LocaleContext} bean into your spring beans. If no localesource is registered the request
     * locale will be used.
     * 
     * @param localeSources the sources to configure
     */
    default void configureLocaleSources(List<LocaleSource> localeSources)
    {
        // Sublcasses override this for custom configuration
    }

    /**
     * Define the mime types, that are subject to compression. Responses matching one of the mime type in this list will
     * be compressed using a algorithm the client requested when applicable. Clearing the list measn no compression will
     * be performed regardless if the client supports it or not.
     * 
     * @param compressableMimeTypes list of mime types that are subject to compression
     */
    default void configureCompressableMimeTypes(List<MimeType> compressableMimeTypes)
    {
        // Sublcasses override this for custom configuration
    }

    /**
     * Let the application override the caching sematics for each available mime type. Per default all cachable
     * mimetypes will get the following Cache-Control header applied: "public, max-age=86400, must-revalidate" This
     * means they will be cached for 24 hours and then the browser sends a validation request.
     * 
     * @param mimeTypeCacheRegistry the registry to add cache configurations to
     */
    default void configureMimeTypeCaching(MimeTypeCacheRegistry mimeTypeCacheRegistry)
    {
        // Sublcasses override this for custom configuration
    }

}
