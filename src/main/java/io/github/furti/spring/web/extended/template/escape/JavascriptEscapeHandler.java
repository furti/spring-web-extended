/**
 *
 */
package io.github.furti.spring.web.extended.template.escape;

import io.github.furti.spring.web.extended.template.ContentEscapeHandler;
import org.owasp.encoder.Encode;

/**
 * @author Daniel Furtlehner
 */
public class JavascriptEscapeHandler implements ContentEscapeHandler {

    @Override
    public String escapeContent(String content) {
        if (content == null) {
            return null;
        }

        content = Encode.forJavaScript(content);

        // A workaround for the controversial https://github.com/OWASP/owasp-java-encoder/issues/22.
        // We handle the encoding of string literal characters ourselves.
        content = content.replace("`", "\\x60");
        content = content.replace("${", "\\x36{");

        return content;
    }
}
