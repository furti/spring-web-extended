/**
 * 
 */
package io.github.furti.spring.web.extended;

import java.util.Map;

import io.github.furti.spring.web.extended.io.ResourceScanner;

/**
 * @author Daniel Furtlehner
 */
public abstract class SpringWebExtendedConfigurerAdapter implements SpringWebExtendedConfigurer
{

    @Override
    public void configureStaticFolders(StaticFolderRegistry registry)
    {
        // Sublcasses override this for custom configuration
    }

    @Override
    public void configureResourceScanners(Map<String, ResourceScanner> scanners)
    {
        // Sublcasses override this for custom configuration
    }

    @Override
    public void configureMimeTypes(Map<String, String> mimeTypes)
    {
        // Sublcasses override this for custom configuration
    }
}
