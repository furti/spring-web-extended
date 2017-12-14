/**
 * 
 */
package io.github.furti.spring.web.extended;

import java.nio.charset.Charset;
import java.util.List;

import io.github.furti.spring.web.extended.io.ResourceScanner;

/**
 * @author Daniel Furtlehner
 */
public interface StaticFolderRegistry
{

    /**
     * Calls {@link #registerFolder(String, String, Charset)} with UTF-8 as charset.
     * 
     * @param basePath the request must start with this path for the resources from this location to be served.
     * @param location the folder to serve resources from. Can be anything a {@link ResourceScanner} can handle.
     * @return the registry for a fluent api
     */
    default StaticFolderRegistry registerFolder(String basePath, String location)
    {
        return registerFolder(basePath, location, Charset.forName("UTF-8"));
    }

    /**
     * @param basePath the request must start with this path for the resources from this location to be served.
     * @param location the folder to serve resources from. Can be anything a {@link ResourceScanner} can handle.
     * @param charset the charset to use for files in this folder
     * @return the registry for a fluent api
     */
    StaticFolderRegistry registerFolder(String basePath, String location, Charset charset);

    /**
     * @return all registered folders
     */
    List<StaticFolder> getFolders();
}
