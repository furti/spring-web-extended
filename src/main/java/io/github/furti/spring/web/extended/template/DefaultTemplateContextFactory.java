/**
 * 
 */
package io.github.furti.spring.web.extended.template;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Daniel Furtlehner
 */
public class DefaultTemplateContextFactory implements TemplateContextFactory
{

    @Override
    public TemplateContext createContext(HttpServletRequest request)
    {
        return new DefaultTemplateContext();
    }

}
