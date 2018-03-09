/**
 * 
 */
package io.github.furti.spring.web.extended.template.escape;

import org.apache.commons.text.StringEscapeUtils;

import io.github.furti.spring.web.extended.template.ContentEscapeHandler;

/**
 * @author Daniel Furtlehner
 */
public class HtmlEscapeHandler implements ContentEscapeHandler
{

    @Override
    public String escapeContent(String content)
    {
        return StringEscapeUtils.escapeHtml4(content);
    }

}
