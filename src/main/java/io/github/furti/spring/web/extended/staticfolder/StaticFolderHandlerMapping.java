/**
 * 
 */
package io.github.furti.spring.web.extended.staticfolder;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * We want to be registered at the end of the handler mapping chain. Because only when nothing else maps we should be
 * asked.
 * 
 * @author Daniel Furtlehner
 */
public class StaticFolderHandlerMapping extends RequestMappingHandlerMapping
{

    public StaticFolderHandlerMapping()
    {
        setOrder(Ordered.LOWEST_PRECEDENCE - 1);
    }

    @Override
    protected boolean isHandler(Class<?> beanType)
    {
        return StaticFoldersController.class.equals(beanType);
    }
}
