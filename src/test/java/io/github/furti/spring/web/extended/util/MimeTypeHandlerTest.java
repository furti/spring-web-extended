/**
 * 
 */
package io.github.furti.spring.web.extended.util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.junit.Assert;
import org.mockito.Mockito;
import org.springframework.util.MimeType;
import org.testng.annotations.Test;

/**
 * @author Daniel Furtlehner
 */
public class MimeTypeHandlerTest
{

    @Test
    public void getMimeType()
    {
        MimeTypeHandler handler =
            new MimeTypeHandler(buildServletContext(), buildDefaultMimeTypes(), buildCacheableMimeTypes());

        {
            // Should use mime type from servlet context as not set in default mime types
            MimeType mimeType = handler.getMimeType("file.html");

            Assert.assertThat(mimeType, equalTo(MimeType.valueOf("text/html")));
        }

        {
            // Should use mime type from servlet context as not set in default mime types
            MimeType mimeType = handler.getMimeType("file.js");

            Assert.assertThat(mimeType, equalTo(MimeType.valueOf("overriden/js")));
        }

        {
            // Should use mime type from servlet context as not set in default mime types
            MimeType mimeType = handler.getMimeType("file.missing");

            Assert.assertThat(mimeType, equalTo(MimeType.valueOf("missing/type")));
        }
    }

    @Test
    public void isCacheable()
    {
        MimeTypeHandler handler =
            new MimeTypeHandler(buildServletContext(), buildDefaultMimeTypes(), buildCacheableMimeTypes());

        {
            // Javascript is not cacheable
            boolean cacheable = handler.shouldBeCached("file.js");

            assertThat(cacheable, equalTo(false));
        }

        {
            // HTML is not cacheable
            boolean cacheable = handler.shouldBeCached("file.html");

            assertThat(cacheable, equalTo(false));
        }

        {
            // CSS is not cacheable
            boolean cacheable = handler.shouldBeCached("file.css");

            assertThat(cacheable, equalTo(true));
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void missingMimeType()
    {
        MimeTypeHandler handler =
            new MimeTypeHandler(buildServletContext(), buildDefaultMimeTypes(), buildCacheableMimeTypes());

        handler.getMimeType("missing.file");
    }

    private Set<MimeType> buildCacheableMimeTypes()
    {
        Set<MimeType> cacheable = new HashSet<>();

        cacheable.add(MimeType.valueOf("text/css"));

        return cacheable;
    }

    private Map<String, String> buildDefaultMimeTypes()
    {
        Map<String, String> mimeTypes = new HashMap<>();

        mimeTypes.put(".missing", "missing/type");
        mimeTypes.put(".js", "overriden/js");

        return mimeTypes;
    }

    private ServletContext buildServletContext()
    {
        ServletContext context = Mockito.mock(ServletContext.class);

        Mockito.when(context.getMimeType("file.js")).thenReturn("application/javascript");
        Mockito.when(context.getMimeType("file.html")).thenReturn("text/html");
        Mockito.when(context.getMimeType("file.css")).thenReturn("text/css");

        return context;
    }
}
