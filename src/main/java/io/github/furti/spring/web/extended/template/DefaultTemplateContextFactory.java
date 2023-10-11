/**
 * 
 */
package io.github.furti.spring.web.extended.template;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.context.i18n.LocaleContext;
import org.springframework.core.io.Resource;
import org.springframework.util.MimeType;

import io.github.furti.spring.web.extended.util.MimeTypeHandler;

/**
 * @author Daniel Furtlehner
 */
public class DefaultTemplateContextFactory implements TemplateContextFactory
{

    private final MimeTypeHandler mimeTypeHandler;
    private final LocaleContext localeContext;

    public DefaultTemplateContextFactory(MimeTypeHandler mimeTypeHandler, LocaleContext localeContext)
    {
        super();
        this.mimeTypeHandler = mimeTypeHandler;
        this.localeContext = localeContext;
    }

    @Override
    public TemplateContext createContext(HttpServletRequest request, Resource resource)
    {
        MimeType mimeType = this.mimeTypeHandler.getMimeType(resource.getFilename());

        DefaultTemplateContext templateContext = new DefaultTemplateContext(mimeType);

        templateContext.addParameter(TemplateContextParameter.LOCALE, localeContext.getLocale());

        return templateContext;
    }

}
