/**
 * 
 */
package io.github.furti.spring.web.extended;

import java.util.Map;

import io.github.furti.spring.web.extended.io.ResourceScanner;
import io.github.furti.spring.web.extended.staticfolder.StaticFoldersController;

/**
 * @author Daniel Furtlehner
 */
public interface SpringWebExtendedConfigurer
{

    /**
     * @param info the application info
     */
    void configureApplication(ApplicationInfo info);

    /**
     * Configure static folders to be served by the {@link StaticFoldersController}.
     * 
     * @param registry the registry that holds all the static folders
     */
    void configureStaticFolders(StaticFolderRegistry registry);

    /**
     * You can add or remove scanners to be used for scanning static folders here. A scanner has a prefix that allows it
     * to be referenced in resource locations. e.g. to scan a folder on the classpath you can use a resource string like
     * so "classpath:some/path/to/the/folder/"
     * 
     * @param scanners the map of scanners. Key is the prefix of the scanner.
     */
    void configureResourceScanners(Map<String, ResourceScanner> scanners);

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
    void configureMimeTypes(Map<String, String> mimeTypes);
}
