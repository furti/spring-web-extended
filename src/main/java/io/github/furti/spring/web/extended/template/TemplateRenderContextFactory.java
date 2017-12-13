package io.github.furti.spring.web.extended.template;

import java.util.Locale;

/**
 * Factory to create a TemplateRenderContext
 *
 * @author Daniel Furtlehner
 */
public interface TemplateRenderContextFactory
{

    TemplateRenderContext createContext(Locale locale, Template template);
}
