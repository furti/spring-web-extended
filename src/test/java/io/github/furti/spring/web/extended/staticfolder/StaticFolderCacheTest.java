/**
 * 
 */
package io.github.furti.spring.web.extended.staticfolder;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.mockito.Mockito;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.Test;

import io.github.furti.spring.web.extended.StaticFolderRegistry;
import io.github.furti.spring.web.extended.expression.DefaultExpressionHandlerRegistry;
import io.github.furti.spring.web.extended.expression.ExpressionHandlerRegistry;
import io.github.furti.spring.web.extended.expression.handlers.MessageExpressionHandler;
import io.github.furti.spring.web.extended.io.ClasspathResourceScanner;
import io.github.furti.spring.web.extended.io.ResourceScanner;
import io.github.furti.spring.web.extended.io.ResourceScanners;
import io.github.furti.spring.web.extended.template.DefaultContentEscapeHandlerRegistry;
import io.github.furti.spring.web.extended.template.DefaultTemplateContextFactory;
import io.github.furti.spring.web.extended.template.TemplateContextFactory;
import io.github.furti.spring.web.extended.template.TemplateFactory;
import io.github.furti.spring.web.extended.template.simple.SimpleTemplateFactory;
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

        StaticFolderCache cache = new StaticFolderCache(registry, buildScanners(), buildMimeTypeHandler(),
            buildTemplateFactory(), buildTemplateContextFactory(), buildResourceTypeRegistry());
        cache.initialize();

        {
            // index.html default
            HttpServletRequest request = buildRequest("/app");
            ResponseEntity<byte[]> actualResponse = cache.render(request);

            assertThat(new String(actualResponse.getBody(), Charset.forName("UTF-8")),
                equalTo("<!doctype html>"
                    + lineSeparator
                    + lineSeparator
                    + "<html>"
                    + lineSeparator
                    + "<head></head>"
                    + lineSeparator
                    + "<body>"
                    + lineSeparator
                    + "    <p>A very useful message</p>"
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
            HttpServletRequest request = buildRequest("/app/index.html");
            ResponseEntity<byte[]> actualResponse = cache.render(request);

            assertThat(new String(actualResponse.getBody(), Charset.forName("UTF-8")),
                equalTo("<!doctype html>"
                    + lineSeparator
                    + lineSeparator
                    + "<html>"
                    + lineSeparator
                    + "<head></head>"
                    + lineSeparator
                    + "<body>"
                    + lineSeparator
                    + "    <p>A very useful message</p>"
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
            // test.js
            HttpServletRequest request = buildRequest("/app/test.js");
            ResponseEntity<byte[]> actualResponse = cache.render(request);

            assertThat(new String(actualResponse.getBody(), Charset.forName("UTF-8")), equalTo("//#message.unknown#"));

            assertThat(actualResponse.getHeaders().getContentType(),
                equalTo(new MediaType("application", "javascript", Charset.forName("UTF-8"))));
        }
    }

    private ResourceTypeRegistry buildResourceTypeRegistry()
    {
        DefaultResourceTypeRegistry registry = new DefaultResourceTypeRegistry();

        registry.resourceTypeByMimeType("application/javascript", ResourceType.TEMPLATE);
        registry.resourceTypeByMimeType("text/html", ResourceType.TEMPLATE);

        return registry;
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void testMissingFolder()
    {
        StaticFolderRegistry registry =
            buildRegistry(false, "/app", "classpath:io/github/furti/spring/web/extended/staticfolder/app/");

        StaticFolderCache cache = new StaticFolderCache(registry, buildScanners(), buildMimeTypeHandler(),
            buildTemplateFactory(), buildTemplateContextFactory(), buildResourceTypeRegistry());
        cache.initialize();

        HttpServletRequest request = buildRequest("/something");
        cache.render(request);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void testMissingResource()
    {
        StaticFolderRegistry registry =
            buildRegistry(false, "/app", "classpath:io/github/furti/spring/web/extended/staticfolder/app/");

        StaticFolderCache cache = new StaticFolderCache(registry, buildScanners(), buildMimeTypeHandler(),
            buildTemplateFactory(), buildTemplateContextFactory(), buildResourceTypeRegistry());
        cache.initialize();

        HttpServletRequest request = buildRequest("/app/missing.js");
        cache.render(request);
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

    private StaticFolderRegistry buildRegistry(boolean reloadMissingResources, String basePath, String location)
    {
        DefaultStaticFolderRegistry registry = new DefaultStaticFolderRegistry();

        registry.reloadOnMissingResource(reloadMissingResources);
        registry.registerFolder(basePath, location);

        return registry;
    }

    private HttpServletRequest buildRequest(String requestURI)
    {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        Mockito.when(request.getRequestURI()).thenReturn(requestURI);

        return request;
    }

    private TemplateContextFactory buildTemplateContextFactory()
    {
        return new DefaultTemplateContextFactory(buildMimeTypeHandler());
    }

    private TemplateFactory buildTemplateFactory()
    {
        StaticMessageSource messageSource = new StaticMessageSource();
        messageSource.addMessage("test", Locale.getDefault(), "A very useful message");

        ExpressionHandlerRegistry registry = new DefaultExpressionHandlerRegistry();
        registry.registerExpressionHandler(new MessageExpressionHandler(messageSource));

        return new SimpleTemplateFactory(registry, new DefaultContentEscapeHandlerRegistry(), '#', '.', '#');
    }
}