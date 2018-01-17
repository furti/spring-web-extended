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
     * @return the rendered template with all expressions replaced. Never null.
     * @throws IOException when an exception occurs rendering the template.
     */
    String render() throws IOException;

    /**
     * This method is called whenever the content of the template needs to be refreshed. The template may decide not to
     * refresh because it is up to date.
     * 
     * @throws IOException when an expection occurs refreshing the template
     */
    void refreshIfNeeded() throws IOException;
}
