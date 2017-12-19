/**
 * 
 */
package io.github.furti.spring.web.extended.template;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;

/**
 * @author Daniel Furtlehner
 */
public interface TemplateContextFactory
{

    /**
     * @param request the request for this template
     * @param resource the resource for the template
     * @return the context
     */
    TemplateContext createContext(HttpServletRequest request, Resource resource);

}
