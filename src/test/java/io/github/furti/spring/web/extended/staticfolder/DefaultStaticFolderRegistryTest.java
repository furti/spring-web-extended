/**
 * 
 */
package io.github.furti.spring.web.extended.staticfolder;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.nio.charset.Charset;
import java.util.List;

import org.testng.annotations.Test;

import io.github.furti.spring.web.extended.StaticFolder;

/**
 * @author Daniel Furtlehner
 */
public class DefaultStaticFolderRegistryTest
{

    @Test
    public void refreshFlagShouldBeSet()
    {
        DefaultStaticFolderRegistry registry = new DefaultStaticFolderRegistry();

        registry.refreshOnMissingResource(false);
        assertThat(registry.isRefreshOnMissingResource(), equalTo(false));

        registry.refreshOnMissingResource(true);
        assertThat(registry.isRefreshOnMissingResource(), equalTo(true));
    }

    @Test
    public void foldersShouldBeAdded()
    {
        DefaultStaticFolderRegistry registry = new DefaultStaticFolderRegistry();
        registry.registerFolder("/first", "classpath:first/");
        registry.registerFolder("/second", "file:second/", Charset.forName("ISO8859-1"));

        List<StaticFolder> actualFolders = registry.getFolders();

        assertThat(actualFolders.size(), equalTo(2));

        StaticFolder actualFolder = actualFolders.get(0);
        assertThat(actualFolder.getBasePath(), equalTo("/first"));
        assertThat(actualFolder.getCharset(), equalTo(Charset.forName("UTF-8")));
        assertThat(actualFolder.getLocation(), equalTo("classpath:first/"));

        actualFolder = actualFolders.get(1);

        assertThat(actualFolder.getBasePath(), equalTo("/second"));
        assertThat(actualFolder.getCharset(), equalTo(Charset.forName("ISO8859-1")));
        assertThat(actualFolder.getLocation(), equalTo("file:second/"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void locationMustEndWithSlash()
    {
        DefaultStaticFolderRegistry registry = new DefaultStaticFolderRegistry();
        registry.registerFolder("/first", "classpath:first");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void pathMustStartWithSlash()
    {
        DefaultStaticFolderRegistry registry = new DefaultStaticFolderRegistry();
        registry.registerFolder("first", "classpath:first/");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void charsetMustBeSet()
    {
        DefaultStaticFolderRegistry registry = new DefaultStaticFolderRegistry();
        registry.registerFolder("/first", "classpath:first/", null);
    }
}
