/**
 *
 */
package io.github.furti.spring.web.extended.template.simple;

import io.github.furti.spring.web.extended.template.ContentEscapeHandler;
import org.springframework.util.MimeType;

/**
 * @author Daniel Furtlehner
 */
public interface ContentEscapeHandlerRegistry {
    /**
     * @param mimeType the mime type to register the handler for
     * @param handler the handler to register
     * @return the registry for a fluent api
     */
    ContentEscapeHandlerRegistry registerHandler(MimeType mimeType, ContentEscapeHandler handler);

    /**
     * @param mimeType the mime type
     * @return the escape handler. Or null if none is registered.
     */
    ContentEscapeHandler getEscapeHandlerForMimeType(MimeType mimeType);
}
