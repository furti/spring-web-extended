/**
 * 
 */
package io.github.furti.spring.web.extended.template.chunk;

import java.nio.charset.Charset;

import org.springframework.core.io.Resource;

import com.x5.template.Theme;

import io.github.furti.spring.web.extended.expression.ExpressionHandler;
import io.github.furti.spring.web.extended.expression.ExpressionHandlerRegistry;
import io.github.furti.spring.web.extended.template.Template;
import io.github.furti.spring.web.extended.template.TemplateContext;
import io.github.furti.spring.web.extended.template.TemplateFactory;

/**
 * @author Daniel Furtlehner
 */
public class ChunkTemplateFactory implements TemplateFactory
{
    private final ExpressionHandlerRegistry registry;

    public ChunkTemplateFactory(ExpressionHandlerRegistry registry)
    {
        super();
        this.registry = registry;

        System.setProperty("chunk.nosnippetcache", "true");
    }

    @Override
    public Template createTemplate(Resource resource, TemplateContext context, Charset charset)
    {
        return new ChunkTemplate(resource, buildTheme(context), charset);
    }

    private Theme buildTheme(TemplateContext context)
    {
        Theme theme = new Theme();

        for (ExpressionHandler handler : registry.getExpressionHandlers())
        {
            theme.addProtocol(new ExpressionHandlerContentSource(context, handler));
        }

        return theme;
    }

}
