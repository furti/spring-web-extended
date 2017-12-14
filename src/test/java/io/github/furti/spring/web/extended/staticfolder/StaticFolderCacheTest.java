/**
 * 
 */
package io.github.furti.spring.web.extended.staticfolder;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.Test;

import io.github.furti.spring.web.extended.StaticFolderRegistry;
import io.github.furti.spring.web.extended.io.ClasspathResourceScanner;
import io.github.furti.spring.web.extended.io.ResourceScanner;
import io.github.furti.spring.web.extended.io.ResourceScanners;
import io.github.furti.spring.web.extended.util.MimeTypeHandler;
import io.github.furti.spring.web.extended.util.ResourceNotFoundException;

/**
 * @author Daniel Furtlehner
 */
public class StaticFolderCacheTest
{
    private final String lineSeparator = System.lineSeparator();

    @Test
    public void testRendering()
    {
        StaticFolderRegistry registry =
            buildRegistry(false, "/app", "classpath:io/github/furti/spring/web/extended/staticfolder/app/");

        StaticFolderCache cache = new StaticFolderCache(registry, buildScanners(), buildMimeTypeHandler());
        cache.initialize();

        {
            // index.html default
            ResponseEntity<String> actualResponse = cache.render("/app");

            assertThat(actualResponse.getBody(),
                equalTo("<!doctype html>"
                    + lineSeparator
                    + lineSeparator
                    + "<html>"
                    + lineSeparator
                    + "<head></head>"
                    + lineSeparator
                    + "<body>"
                    + lineSeparator
                    + "    <p>{.message.test}</p>"
                    + lineSeparator
                    + "    <p>Some Text with special chars äöü.</p>"
                    + lineSeparator
                    + "</body>"
                    + lineSeparator
                    + "</html>"));

            assertThat(actualResponse.getHeaders().getContentType(),
                equalTo(new MediaType("text", "html", Charset.forName("UTF-8"))));
        }

        {
            // index.html
            ResponseEntity<String> actualResponse = cache.render("/app/index.html");

            assertThat(actualResponse.getBody(),
                equalTo("<!doctype html>"
                    + lineSeparator
                    + lineSeparator
                    + "<html>"
                    + lineSeparator
                    + "<head></head>"
                    + lineSeparator
                    + "<body>"
                    + lineSeparator
                    + "    <p>{.message.test}</p>"
                    + lineSeparator
                    + "    <p>Some Text with special chars äöü.</p>"
                    + lineSeparator
                    + "</body>"
                    + lineSeparator
                    + "</html>"));

            assertThat(actualResponse.getHeaders().getContentType(),
                equalTo(new MediaType("text", "html", Charset.forName("UTF-8"))));
        }

        {
            // index.html
            ResponseEntity<String> actualResponse = cache.render("/app/test.js");

            assertThat(actualResponse.getBody(), equalTo("//{.message.test}"));

            assertThat(actualResponse.getHeaders().getContentType(),
                equalTo(new MediaType("application", "javascript", Charset.forName("UTF-8"))));
        }
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void testMissingFolder()
    {
        StaticFolderRegistry registry =
            buildRegistry(false, "/app", "classpath:io/github/furti/spring/web/extended/staticfolder/app/");

        StaticFolderCache cache = new StaticFolderCache(registry, buildScanners(), buildMimeTypeHandler());
        cache.initialize();

        cache.render("/something");
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void testMissingResource()
    {
        StaticFolderRegistry registry =
            buildRegistry(false, "/app", "classpath:io/github/furti/spring/web/extended/staticfolder/app/");

        StaticFolderCache cache = new StaticFolderCache(registry, buildScanners(), buildMimeTypeHandler());
        cache.initialize();

        cache.render("/app/missing.js");
    }

    private MimeTypeHandler buildMimeTypeHandler()
    {
        Map<String, String> mimeTypes = new HashMap<>();
        mimeTypes.put(".js", "application/javascript");
        mimeTypes.put(".html", "text/html");

        return new MimeTypeHandler(Mockito.mock(ServletContext.class), mimeTypes);
    }

    private ResourceScanners buildScanners()
    {
        Map<String, ResourceScanner> scanners = new HashMap<>();
        scanners.put("classpath", new ClasspathResourceScanner());

        return new ResourceScanners(scanners);
    }

    private StaticFolderRegistry buildRegistry(boolean refreshMissingResources, String basePath, String location)
    {
        DefaultStaticFolderRegistry registry = new DefaultStaticFolderRegistry();

        registry.refreshOnMissingResource(refreshMissingResources);
        registry.registerFolder(basePath, location);

        return registry;
    }
}
