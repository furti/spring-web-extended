/**
 * 
 */
package io.github.furti.spring.web.extended.template;

import org.springframework.util.MimeType;

/**
 * @author Daniel Furtlehner
 */
public interface TemplateContext
{
    MimeType getMimeType();

    Object getParameter(Object key);
}
