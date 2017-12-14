/**
 * 
 */
package io.github.furti.spring.web.extended.util;

import static org.hamcrest.CoreMatchers.*;

import java.util.HashMap;
import java.util.Map;

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
        MimeTypeHandler handler = new MimeTypeHandler(buildServletContext(), buildDefaultMimeTypes());

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

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void missingMimeType()
    {
        MimeTypeHandler handler = new MimeTypeHandler(buildServletContext(), buildDefaultMimeTypes());

        handler.getMimeType("missing.file");
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

        return context;
    }
}
