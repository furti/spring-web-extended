/**
 * 
 */
package io.github.furti.spring.web.extended.staticfolder;

import java.util.Collection;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.DelegatingMessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import io.github.furti.spring.web.extended.MessageRegistry;
import io.github.furti.spring.web.extended.StaticFolderRegistry;
import io.github.furti.spring.web.extended.expression.ExpressionHandlerConfig;
import io.github.furti.spring.web.extended.io.ResourceScannerConfig;
import io.github.furti.spring.web.extended.io.ResourceScanners;
import io.github.furti.spring.web.extended.template.TemplateContextFactory;
import io.github.furti.spring.web.extended.template.TemplateFactory;
import io.github.furti.spring.web.extended.template.simple.ContentEscapeHandlerRegistry;
import io.github.furti.spring.web.extended.util.MimeTypeHandler;

/**
 * A Configuration that enables static folder style. Static folder style means, that there is a single root folder per
 * single page application. This folder contains a index.html file that will be served by default and subsequent
 * requests for resources will be served from this folder. This is useful for angular-cli apps and other modern web
 * frameworks.
 * 
 * @author Daniel Furtlehner
 */
@Configuration
@Import({ResourceScannerConfig.class, ExpressionHandlerConfig.class})
@ComponentScan(basePackageClasses = StaticFolderConfiguration.class)
public class StaticFolderConfiguration extends WebMvcConfigurerAdapter
{

    @Autowired
    private StaticFolderConfigurerConfiguration configurerConfiguration;

    @Bean
    public StaticFolderCache staticFolderCache(ResourceScanners scanners, MimeTypeHandler mimeTypeHandler,
        TemplateFactory templateFactory, TemplateContextFactory contextFactory)
    {
        StaticFolderRegistry staticFolderRegistry = configurerConfiguration.getStaticFolderRegistry();

        return new StaticFolderCache(staticFolderRegistry, scanners, mimeTypeHandler, templateFactory, contextFactory);
    }

    @Bean
    public MimeTypeHandler mimeTypeHandler(ServletContext servletContext)
    {
        return new MimeTypeHandler(servletContext, configurerConfiguration.getMimeTypes());
    }

    @Bean
    public ContentEscapeHandlerRegistry contentEscapeHandlers()
    {
        return configurerConfiguration.getContentExceptHandlerRegistry();
    }

    @Bean
    public MessageSource messageSource()
    {
        DelegatingMessageSource messageSource = new DelegatingMessageSource();

        MessageRegistry registry = configurerConfiguration.getMessageRegistry();

        if (registry.getMessageSource() != null)
        {
            messageSource.setParentMessageSource(registry.getMessageSource());
        }
        else
        {
            ReloadableResourceBundleMessageSource delegate = new ReloadableResourceBundleMessageSource();
            delegate.setFallbackToSystemLocale(false);
            delegate.setDefaultEncoding(registry.getEncoding());

            Collection<String> basenames = registry.getBasenames();

            if (!basenames.isEmpty())
            {
                delegate.addBasenames(basenames.toArray(new String[basenames.size()]));
            }

            messageSource.setParentMessageSource(delegate);
        }

        return messageSource;
    }
}
