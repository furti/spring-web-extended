/**
 *
 */
package io.github.furti.spring.web.extended.staticfolder;

import io.github.furti.spring.web.extended.StaticFolder;
import io.github.furti.spring.web.extended.StaticFolderRegistry;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Daniel Furtlehner
 */
public class DefaultStaticFolderRegistry implements StaticFolderRegistry {

    private final List<StaticFolder> folders = new ArrayList<>();
    private boolean reloadOnMissingResource;
    private long resourceRefreshInterval;

    @Override
    public StaticFolderRegistry registerFolder(
        String basePath,
        String location,
        Charset charset,
        String... indexFallbacks
    ) {
        folders.add(new StaticFolder(basePath, location, charset, indexFallbacks));

        return this;
    }

    @Override
    public List<StaticFolder> getFolders() {
        return Collections.unmodifiableList(folders);
    }

    @Override
    public String toString() {
        return "DefaultStaticFolderRegistry [folders=" + folders + "]";
    }

    @Override
    public StaticFolderRegistry reloadOnMissingResource(boolean refresh) {
        this.reloadOnMissingResource = refresh;

        return this;
    }

    @Override
    public boolean isReloadOnMissingResource() {
        return reloadOnMissingResource;
    }

    @Override
    public StaticFolderRegistry resourceRefreshInterval(long intervalInSeconds) {
        this.resourceRefreshInterval = intervalInSeconds;

        return this;
    }

    @Override
    public long getResourceRefreshInterval() {
        return resourceRefreshInterval;
    }
}
