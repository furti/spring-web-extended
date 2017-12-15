/**
 * 
 */
package io.github.furti.spring.web.extended.template;

import java.io.IOException;

/**
 * @author Daniel Furtlehner
 */
public interface Template
{

    /**
     * @return the rendered template with all expressions replaced.
     * @throws IOException when an exception occurs rendering the template.
     */
    String render() throws IOException;
}
