/**
 * 
 */
package io.github.furti.spring.web.extended.staticfolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.github.furti.spring.web.extended.SpringWebExtendedConfigurer;
import io.github.furti.spring.web.extended.StaticFolderRegistry;
import io.github.furti.spring.web.extended.io.ResourceScanner;

/**
 * @author Daniel Furtlehner
 */
public class DelegatingSpringWebExtendedConfigurer implements SpringWebExtendedConfigurer
{

    private final List<SpringWebExtendedConfigurer> configurers = new ArrayList<>();

    void addConfigurers(List<SpringWebExtendedConfigurer> configurersToAdd)
    {
        configurers.addAll(configurersToAdd);
    }

    @Override
    public void configureStaticFolders(StaticFolderRegistry registry)
    {
        for (SpringWebExtendedConfigurer configurer : configurers)
        {
            configurer.configureStaticFolders(registry);
        }
    }

    @Override
    public void configureResourceScanners(Map<String, ResourceScanner> scanners)
    {
        for (SpringWebExtendedConfigurer configurer : configurers)
        {
            configurer.configureResourceScanners(scanners);
        }
    }

    @Override
    public void configureMimeTypes(Map<String, String> mimeTypes)
    {
        for (SpringWebExtendedConfigurer configurer : configurers)
        {
            configurer.configureMimeTypes(mimeTypes);
        }
    }

}
