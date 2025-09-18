/**
 *
 */
package io.github.furti.spring.web.extended.template;

import java.io.IOException;

/**
 * @author Daniel Furtlehner
 */
public interface Template {
    /**
     * @return the rendered template with all expressions replaced. Never null.
     * @throws IOException when an exception occurs rendering the template.
     */
    String render() throws IOException;

    /**
     * @return the refresh time in milliseconds
     */
    long getLastRefreshed();

    /**
     * This method is called whenever the content of the template needs to be refreshed. The template may decide not to
     * refresh because it is up to date.
     *
     * @return true when a refresh was performed. False otherwise.
     * @throws IOException when an expection occurs refreshing the template
     */
    boolean refreshIfNeeded() throws IOException;

    /**
     * Forces a refresh of the template regardless of a change of the template itself.
     *
     * @throws IOException when an expection occurs refreshing the template
     */
    void forceRefresh() throws IOException;
}
