/**
 * 
 */
package io.github.furti.spring.web.extended.staticfolder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import io.github.furti.spring.web.extended.annotation.DefaultBean;
import io.github.furti.spring.web.extended.expression.ExpressionHandlerRegistry;
import io.github.furti.spring.web.extended.template.DefaultTemplateContextFactory;
import io.github.furti.spring.web.extended.template.TemplateContextFactory;
import io.github.furti.spring.web.extended.template.TemplateFactory;
import io.github.furti.spring.web.extended.template.simple.ContentEscapeHandlerRegistry;
import io.github.furti.spring.web.extended.template.simple.SimpleTemplateFactory;
import io.github.furti.spring.web.extended.util.MimeTypeHandler;

/**
 * @author Daniel Furtlehner
 */
@Configuration
@Order(Ordered.LOWEST_PRECEDENCE)
public class StaticFolderDefaultsConfig
{

    @Bean
    @DefaultBean(TemplateFactory.class)
    public TemplateFactory defaultTemplateFactory(ExpressionHandlerRegistry expressionHandlers,
        ContentEscapeHandlerRegistry escapeHandlers)
    {
        return new SimpleTemplateFactory(expressionHandlers, escapeHandlers, '#', ':', '#');
    }

    @Bean
    @DefaultBean(TemplateContextFactory.class)
    public TemplateContextFactory defaultTemplateContextFactory(MimeTypeHandler mimeTypeHandler,
        LocaleContext localeContext)
    {
        return new DefaultTemplateContextFactory(mimeTypeHandler, localeContext);
    }
}
