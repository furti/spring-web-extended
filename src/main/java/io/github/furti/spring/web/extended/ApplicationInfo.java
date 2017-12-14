/**
 * 
 */
package io.github.furti.spring.web.extended;

/**
 * @author Daniel Furtlehner
 */
public interface ApplicationInfo
{

    /**
     * Enables or disables the production mode. This info is used to perform some optimizations like caching resources
     * and so on. Can be set via command line by enabling one of the following spring profiles "optimizeresources",
     * "prod", "production"
     * 
     * @param productionMode true if the application should work in production mode.
     * @return the info for a fluent api.
     */
    ApplicationInfo productionMode(boolean productionMode);

    /**
     * @return true if the production mode is enabled.
     */
    boolean isProductionMode();
}
