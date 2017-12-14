/**
 * 
 */
package io.github.furti.spring.web.extended;

import java.util.List;

import io.github.furti.spring.web.extended.io.ResourceScanner;

/**
 * @author Daniel Furtlehner
 */
public interface StaticFolderRegistry
{

    /**
     * @param basePath the request must start with this path for the resources from this location to be served.
     * @param location the folder to serve resources from. Can be anything a {@link ResourceScanner} can handle.
     * @return the registry for a fluent api
     */
    StaticFolderRegistry registerFolder(String basePath, String location);

    /**
     * @return all registered folders
     */
    List<StaticFolder> getFolders();
}
