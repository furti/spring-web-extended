/**
 *
 */
package io.github.furti.spring.web.extended;

import java.util.Collection;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * @author Daniel Furtlehner
 */
public interface MessageRegistry {
    /**
     * Id a message source is set it will be used. Otherwise a {@link ReloadableResourceBundleMessageSource} will be
     * created with the configuration of this registry.
     *
     * @param messageSource the message source to use.
     */
    void setMessageSource(MessageSource messageSource);

    /**
     * @return the message source to use. Can be null to create a default message source;
     */
    MessageSource getMessageSource();

    /**
     * @param encoding the encoding to set
     * @return the registry for a fluent api
     */
    MessageRegistry setEncoding(String encoding);

    /**
     * @return the encoding of files for the default message source.
     */
    String getEncoding();

    /**
     * @param basename the basename to add
     * @return the registry for a fluent api
     */
    MessageRegistry addBasename(String basename);

    /**
     * @param basename basename to remove
     * @return the registry for a fluent api
     */
    MessageRegistry removeBasename(String basename);

    /**
     * @return basenames to add to the default message source
     */
    Collection<String> getBasenames();
}
