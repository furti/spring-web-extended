/**
 * 
 */
package io.github.furti.spring.web.extended.template;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Daniel Furtlehner
 */
public interface TemplateContextFactory
{

    /**
     * @param request the request for this template
     * @return the context
     */
    TemplateContext createContext(HttpServletRequest request);

}
