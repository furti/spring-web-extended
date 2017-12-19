/**
 * 
 */
package io.github.furti.spring.web.extended.template;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.util.MimeType;

import io.github.furti.spring.web.extended.util.MimeTypeHandler;

/**
 * @author Daniel Furtlehner
 */
public class DefaultTemplateContextFactory implements TemplateContextFactory
{

    private final MimeTypeHandler mimeTypeHandler;

    public DefaultTemplateContextFactory(MimeTypeHandler mimeTypeHandler)
    {
        super();
        this.mimeTypeHandler = mimeTypeHandler;
    }

    @Override
    public TemplateContext createContext(HttpServletRequest request, Resource resource)
    {
        MimeType mimeType = this.mimeTypeHandler.getMimeType(resource.getFilename());

        DefaultTemplateContext templateContext = new DefaultTemplateContext(mimeType);

        //TODO: should we get the locale from the url as well?
        templateContext.addParameter(TemplateContextParameter.LOCALE, request.getLocale());

        return templateContext;
    }

}
