/**
 * 
 */
package io.github.furti.spring.web.extended;

import java.util.Map;

import io.github.furti.spring.web.extended.expression.ExpressionHandlerRegistry;
import io.github.furti.spring.web.extended.io.ResourceScanner;
import io.github.furti.spring.web.extended.staticfolder.ResourceTypeRegistry;
import io.github.furti.spring.web.extended.template.DefaultContentEscapeHandlerRegistry;

/**
 * @author Daniel Furtlehner
 */
public abstract class SpringWebExtendedConfigurerAdapter implements SpringWebExtendedConfigurer
{

    @Override
    public void configureApplication(ApplicationInfo info)
    {
        // Sublcasses override this for custom configuration

    }

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

    @Override
    public void configureExpressionHandlers(ExpressionHandlerRegistry registry)
    {
        // Sublcasses override this for custom configuration
    }

    @Override
    public void configureMessages(MessageRegistry messageRegistry)
    {
        // Sublcasses override this for custom configuration
    }

    @Override
    public void configureContentEscapeHandlers(DefaultContentEscapeHandlerRegistry registry)
    {
        // Sublcasses override this for custom configuration
    }

    @Override
    public void configureResourceTypes(ResourceTypeRegistry registry)
    {
        // Sublcasses override this for custom configuration        
    }
}
