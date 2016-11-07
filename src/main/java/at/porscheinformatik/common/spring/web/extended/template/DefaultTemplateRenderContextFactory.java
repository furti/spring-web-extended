package at.porscheinformatik.common.spring.web.extended.template;

import java.util.Locale;

/**
 * Default Implementation of a {@link TemplateRenderContextFactory}
 * <p>
 * Create a {@link DefaultTemplateRenderContext} with the locale and the template type
 * </p>
 *
 * @author Daniel Furtlehner
 */
public class DefaultTemplateRenderContextFactory implements TemplateRenderContextFactory
{

    @Override
    public TemplateRenderContext createContext(Locale locale, Template template)
    {
        return new DefaultTemplateRenderContext(locale, template.getType());
    }
}
