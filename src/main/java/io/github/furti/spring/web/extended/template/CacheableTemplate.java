/**
 *
 */
package io.github.furti.spring.web.extended.template;

import io.github.furti.spring.web.extended.staticfolder.CommonContentCache;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;

/**
 * @author Daniel Furtlehner
 */
public abstract class CacheableTemplate implements Template {

    private final Object lock = new Object();

    protected final Resource resource;
    protected final Charset charset;
    private String content = "";
    private long lastModified;
    private long lastRefreshed;

    public CacheableTemplate(Resource resource, Charset charset) {
        super();
        this.resource = resource;
        this.charset = charset;
    }

    @Override
    public String render() throws IOException {
        return content;
    }

    @Override
    public boolean refreshIfNeeded() throws IOException {
        synchronized (lock) {
            if (lastModified == 0 || lastModified != resource.lastModified()) {
                String newContent = doRender();

                content = CommonContentCache.getCommonContent(newContent);

                lastModified = resource.lastModified();
                lastRefreshed = System.currentTimeMillis();

                return true;
            }

            return false;
        }
    }

    @Override
    public void forceRefresh() throws IOException {
        synchronized (lock) {
            String newContent = doRender();

            content = CommonContentCache.getCommonContent(newContent);

            lastModified = resource.lastModified();
            lastRefreshed = System.currentTimeMillis();
        }
    }

    @Override
    public long getLastRefreshed() {
        return lastRefreshed;
    }

    protected abstract String doRender() throws IOException;

    protected String loadTemplate() throws IOException {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), charset)) {
            return IOUtils.toString(reader);
        }
    }
}
