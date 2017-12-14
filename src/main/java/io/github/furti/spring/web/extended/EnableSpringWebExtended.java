/**
 * 
 */
package io.github.furti.spring.web.extended;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import io.github.furti.spring.web.extended.staticfolder.StaticFolderConfiguration;
import io.github.furti.spring.web.extended.staticfolder.StaticFolderSpringWebExtendedImportSelector;

/**
 * Add this annotation to @{@link Configuration} classes to enable extended web support configured in
 * {@link StaticFolderConfiguration}.
 * <p>
 * You can customize the configuration by implementing the {@link SpringWebExtendedConfigurer} or extending the
 * {@link SpringWebExtendedConfigurerAdapter}
 * </p>
 *
 * <pre>
 * &#064;Configuration
 * &#064;EnableSpringWebExtended
 * &#064;ComponentScan(basePackageClasses = {AppConfig.class})
 * public class AppConfig extends SpringWebExtendedConfigurerAdapter
 * {
 *     &#064;Override
 *     public void configureStaticFolders(StaticFolderRegistry registry)
 *     {
 *         registry.registerFolder("/login", "classpath:path/to/login/resources/");
 *     }
 * }
 * </pre>
 * 
 * @author Daniel Furtlehner
 */
@Retention(RUNTIME)
@Target(TYPE)
@Import(value = {StaticFolderSpringWebExtendedImportSelector.class})
@EnableWebMvc
public @interface EnableSpringWebExtended
{

}
