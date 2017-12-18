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
     * 0 means no caching at all. A negative number means infinite caching.
     * 
     * @param intervalInSeconds the time between two template refresh cycles. Defaults to no caching in development mode
     *            and a interval of 10 minutes in production mode.
     * @return the registry for a fluent api.
     */
    StaticFolderRegistry templateRefreshInterval(long intervalInSeconds);

    /**
     * @return the time between two template refresh cycles.
     */
    long getTemplateRefreshInterval();

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

    /**
     * @param refresh true if we should reload folders when a missing resource is requested. Especially useful for
     *            development. Defaults to false in production mode and true in development mode.
     * @return the registry for a fluent api
     */
    StaticFolderRegistry reloadOnMissingResource(boolean refresh);

    /**
     * @return true if we should reload folders when a missing resource is requested.
     */
    boolean isReloadOnMissingResource();
}
