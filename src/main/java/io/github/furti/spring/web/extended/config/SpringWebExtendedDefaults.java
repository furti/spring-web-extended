package io.github.furti.spring.web.extended.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import io.github.furti.spring.web.extended.annotation.DefaultBean;
import io.github.furti.spring.web.extended.http.DefaultLinkCreator;
import io.github.furti.spring.web.extended.http.LinkCreator;
import io.github.furti.spring.web.extended.template.legacy.DefaultTemplateRenderContextFactory;
import io.github.furti.spring.web.extended.template.legacy.TemplateFactory;
import io.github.furti.spring.web.extended.template.legacy.TemplateRenderContextFactory;
import io.github.furti.spring.web.extended.template.legacy.chunk.ChunkTemplateFactory;

@Configuration
@Order(Ordered.LOWEST_PRECEDENCE)
public class SpringWebExtendedDefaults
{

    @Bean
    @DefaultBean(TemplateFactory.class)
    public TemplateFactory defaultTemplateFactory()
    {
        return new ChunkTemplateFactory();
    }

    @Bean
    @DefaultBean(LinkCreator.class)
    public LinkCreator defaultLinkCreator()
    {
        return new DefaultLinkCreator();
    }

    @Bean
    @DefaultBean(TemplateRenderContextFactory.class)
    public TemplateRenderContextFactory defaultTemplateRenderContextFactory()
    {
        return new DefaultTemplateRenderContextFactory();
    }
}
