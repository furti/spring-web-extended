package at.porscheinformatik.common.spring.web.extended.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import at.porscheinformatik.common.spring.web.extended.annotation.DefaultBean;
import at.porscheinformatik.common.spring.web.extended.http.DefaultLinkCreator;
import at.porscheinformatik.common.spring.web.extended.http.LinkCreator;
import at.porscheinformatik.common.spring.web.extended.template.DefaultTemplateRenderContextFactory;
import at.porscheinformatik.common.spring.web.extended.template.TemplateFactory;
import at.porscheinformatik.common.spring.web.extended.template.TemplateRenderContextFactory;
import at.porscheinformatik.common.spring.web.extended.template.chunk.ChunkTemplateFactory;

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
