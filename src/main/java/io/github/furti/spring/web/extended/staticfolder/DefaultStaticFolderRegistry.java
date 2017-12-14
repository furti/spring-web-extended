/**
 * 
 */
package io.github.furti.spring.web.extended.staticfolder;

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
    public StaticFolderRegistry registerFolder(String basePath, String location)
    {
        folders.add(new StaticFolder(basePath, location));

        return this;
    }

    @Override
    public String toString()
    {
        return "DefaultStaticFolderRegistry [folders=" + folders + "]";
    }

    @Override
    public List<StaticFolder> getFolders()
    {
        return Collections.unmodifiableList(folders);
    }

}
