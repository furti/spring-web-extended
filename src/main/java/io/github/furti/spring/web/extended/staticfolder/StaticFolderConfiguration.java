/**
 * 
 */
package io.github.furti.spring.web.extended.staticfolder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import io.github.furti.spring.web.extended.StaticFolderRegistry;
import io.github.furti.spring.web.extended.io.ResourceScannerConfig;
import io.github.furti.spring.web.extended.io.ResourceScanners;

/**
 * A Configuration that enables static folder style. Static folder style means, that there is a single root folder per
 * single page application. This folder contains a index.html file that will be served by default and subsequent
 * requests for resources will be served from this folder. This is useful for angular-cli apps and other modern web
 * frameworks.
 * 
 * @author Daniel Furtlehner
 */
@Configuration
@Import({ResourceScannerConfig.class})
@ComponentScan(basePackageClasses = StaticFolderConfiguration.class)
public class StaticFolderConfiguration extends WebMvcConfigurerAdapter
{

    @Autowired
    private StaticFolderConfigurerConfiguration configurerConfiguration;

    @Bean
    public StaticFolderCache staticFolderCache(ResourceScanners scanners, MimeTypeHandler mimeTypeHandler)
    {
        StaticFolderRegistry staticFolderRegistry = configurerConfiguration.getStaticFolderRegistry();

        return new StaticFolderCache(staticFolderRegistry, scanners, mimeTypeHandler);
    }
}
