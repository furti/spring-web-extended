/**
 * 
 */
package io.github.furti.spring.web.extended.template.escape;

import org.owasp.encoder.Encode;

import io.github.furti.spring.web.extended.template.ContentEscapeHandler;

/**
 * @author Daniel Furtlehner
 */
public class HtmlEscapeHandler implements ContentEscapeHandler
{

    @Override
    public String escapeContent(String content)
    {
        return Encode.forHtml(content);
    }

}
