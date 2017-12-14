/**
 * 
 */
package io.github.furti.spring.web.extended.staticfolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import io.github.furti.spring.web.extended.SpringWebExtendedConfigurer;
import io.github.furti.spring.web.extended.StaticFolderRegistry;
import io.github.furti.spring.web.extended.io.ResourceScanner;

/**
 * @author Daniel Furtlehner
 */
@Configuration
public class StaticFolderConfigurerConfiguration
{
    private final DelegatingSpringWebExtendedConfigurer configurer = new DelegatingSpringWebExtendedConfigurer();
    private DefaultStaticFolderRegistry staticFolderRegistry;
    private Map<String, String> mimeTypes;

    @Autowired(required = false)
    public void setConfigurers(List<SpringWebExtendedConfigurer> configurers)
    {
        configurer.addConfigurers(configurers);
    }

    public StaticFolderRegistry getStaticFolderRegistry()
    {
        if (staticFolderRegistry == null)
        {
            staticFolderRegistry = new DefaultStaticFolderRegistry();

            configurer.configureStaticFolders(staticFolderRegistry);
        }

        return staticFolderRegistry;
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

    public void configureResourceScanners(Map<String, ResourceScanner> scanners)
    {
        configurer.configureResourceScanners(scanners);
    }
}
