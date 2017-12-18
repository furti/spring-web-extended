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
        //TODO: should we get the locale from the url as well?
        DefaultTemplateContext templateContext = new DefaultTemplateContext();

        templateContext.addParameter(TemplateContextParameter.LOCALE, request.getLocale());

        return templateContext;
    }

}
