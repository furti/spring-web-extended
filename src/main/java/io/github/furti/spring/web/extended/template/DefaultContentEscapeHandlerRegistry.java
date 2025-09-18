/**
 *
 */
package io.github.furti.spring.web.extended.template;

import io.github.furti.spring.web.extended.template.simple.ContentEscapeHandlerRegistry;
import java.util.HashMap;
import java.util.Map;
import org.springframework.util.MimeType;

/**
 * @author Daniel Furtlehner
 */
public class DefaultContentEscapeHandlerRegistry implements ContentEscapeHandlerRegistry {

    private final Map<MimeType, ContentEscapeHandler> handlers = new HashMap<>();

    @Override
    public ContentEscapeHandlerRegistry registerHandler(MimeType mimeType, ContentEscapeHandler handler) {
        handlers.put(mimeType, handler);

        return this;
    }

    @Override
    public ContentEscapeHandler getEscapeHandlerForMimeType(MimeType mimeType) {
        return handlers.get(mimeType);
    }
}
