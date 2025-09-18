/**
 *
 */
package io.github.furti.spring.web.extended.template.escape;

import io.github.furti.spring.web.extended.template.ContentEscapeHandler;
import org.owasp.encoder.Encode;

/**
 * @author Daniel Furtlehner
 */
public class HtmlEscapeHandler implements ContentEscapeHandler {

    @Override
    public String escapeContent(String content) {
        return Encode.forHtml(content);
    }
}
