/**
 * 
 */
package io.github.furti.spring.web.extended.staticfolder;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.furti.spring.web.extended.StaticFolder;
import io.github.furti.spring.web.extended.StaticFolderRegistry;

/**
 * @author Daniel Furtlehner
 */
public class DefaultStaticFolderRegistry implements StaticFolderRegistry
{
    private final List<StaticFolder> folders = new ArrayList<>();

    @Override
    public StaticFolderRegistry registerFolder(String basePath, String location, Charset charset)
    {
        folders.add(new StaticFolder(basePath, location, charset));

        return this;
    }

    @Override
    public List<StaticFolder> getFolders()
    {
        return Collections.unmodifiableList(folders);
    }

    @Override
    public String toString()
    {
        return "DefaultStaticFolderRegistry [folders=" + folders + "]";
    }

}
