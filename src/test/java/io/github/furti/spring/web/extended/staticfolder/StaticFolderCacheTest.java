/**
 * 
 */
package io.github.furti.spring.web.extended.staticfolder;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;

import io.github.furti.spring.web.extended.ApplicationInfo;
import io.github.furti.spring.web.extended.StaticFolderRegistry;
import io.github.furti.spring.web.extended.compression.CompressionManager;
import io.github.furti.spring.web.extended.compression.DefaultCompressionManager;
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
    /**
     * 
     */
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private static final ThreadLocal<Locale> TEST_LOCALE = new ThreadLocal<>();
    private final String lineSeparator = System.lineSeparator();

    @Test
    public void testRenderingInProductionMode()
    {
        StaticFolderRegistry registry =
            buildRegistry(false, true, "/app", "classpath:io/github/furti/spring/web/extended/staticfolder/app/",
                "/indexfallback", "/indexfallback/", "/nextfallback/*", "/lastfallback/**");

        StaticFolderCache cache = new StaticFolderCache(registry, buildScanners(), buildMimeTypeHandler(),
            buildTemplateFactory(), buildTemplateContextFactory(), buildResourceTypeRegistry(), buildAppInfo(true),
            buildCompressionManager());
        cache.initialize();

        try
        {
            {
                TEST_LOCALE.set(Locale.ENGLISH);
                // index.html default
                HttpServletRequest request = buildRequest("/app");
                ResponseEntity<byte[]> actualResponse = cache.render(request);

                assertThat(new String(actualResponse.getBody(), UTF_8),
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

                assertThat(actualResponse.getHeaders().getContentType(), equalTo(new MediaType("text", "html", UTF_8)));
                assertNotCacheable(actualResponse);
            }

            {
                TEST_LOCALE.set(Locale.GERMAN);
                // index.html default
                HttpServletRequest request = buildRequest("/app");
                ResponseEntity<byte[]> actualResponse = cache.render(request);

                assertThat(new String(actualResponse.getBody(), UTF_8),
                    equalTo("<!doctype html>"
                        + lineSeparator
                        + lineSeparator
                        + "<html>"
                        + lineSeparator
                        + "<head></head>"
                        + lineSeparator
                        + "<body>"
                        + lineSeparator
                        + "    <p>Eine sehr nützliche Nachricht</p>"
                        + lineSeparator
                        + "    <p>Some Text with special chars äöü.</p>"
                        + lineSeparator
                        + "</body>"
                        + lineSeparator
                        + "</html>"));

                assertThat(actualResponse.getHeaders().getContentType(), equalTo(new MediaType("text", "html", UTF_8)));
                assertNotCacheable(actualResponse);
            }

            {
                TEST_LOCALE.set(Locale.ENGLISH);
                // index.html with trailing /
                HttpServletRequest request = buildRequest("/app/");
                ResponseEntity<byte[]> actualResponse = cache.render(request);

                assertThat(new String(actualResponse.getBody(), UTF_8),
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

                assertThat(actualResponse.getHeaders().getContentType(), equalTo(new MediaType("text", "html", UTF_8)));
                assertNotCacheable(actualResponse);
            }

            {
                TEST_LOCALE.set(Locale.GERMAN);
                // index.html with trailing /
                HttpServletRequest request = buildRequest("/app/");
                ResponseEntity<byte[]> actualResponse = cache.render(request);

                assertThat(new String(actualResponse.getBody(), UTF_8),
                    equalTo("<!doctype html>"
                        + lineSeparator
                        + lineSeparator
                        + "<html>"
                        + lineSeparator
                        + "<head></head>"
                        + lineSeparator
                        + "<body>"
                        + lineSeparator
                        + "    <p>Eine sehr nützliche Nachricht</p>"
                        + lineSeparator
                        + "    <p>Some Text with special chars äöü.</p>"
                        + lineSeparator
                        + "</body>"
                        + lineSeparator
                        + "</html>"));

                assertThat(actualResponse.getHeaders().getContentType(), equalTo(new MediaType("text", "html", UTF_8)));
                assertNotCacheable(actualResponse);
            }

            {
                TEST_LOCALE.set(Locale.ENGLISH);
                // index.html
                HttpServletRequest request = buildRequest("/app/index.html");
                ResponseEntity<byte[]> actualResponse = cache.render(request);

                assertThat(new String(actualResponse.getBody(), UTF_8),
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

                assertThat(actualResponse.getHeaders().getContentType(), equalTo(new MediaType("text", "html", UTF_8)));
                assertNotCacheable(actualResponse);
            }

            {
                TEST_LOCALE.set(Locale.ENGLISH);
                // index.html for supath /indexfallback
                HttpServletRequest request = buildRequest("/app/indexfallback");
                ResponseEntity<byte[]> actualResponse = cache.render(request);

                assertThat(new String(actualResponse.getBody(), UTF_8),
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

                assertThat(actualResponse.getHeaders().getContentType(), equalTo(new MediaType("text", "html", UTF_8)));
                assertNotCacheable(actualResponse);
            }

            {
                TEST_LOCALE.set(Locale.ENGLISH);
                // index.html for supath /indexfallback/
                HttpServletRequest request = buildRequest("/app/indexfallback/");
                ResponseEntity<byte[]> actualResponse = cache.render(request);

                assertThat(new String(actualResponse.getBody(), UTF_8),
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

                assertThat(actualResponse.getHeaders().getContentType(), equalTo(new MediaType("text", "html", UTF_8)));
                assertNotCacheable(actualResponse);
            }

            {
                TEST_LOCALE.set(Locale.ENGLISH);
                // index.html with fallback
                HttpServletRequest request = buildRequest("/app/nextfallback/");
                ResponseEntity<byte[]> actualResponse = cache.render(request);

                assertThat(new String(actualResponse.getBody(), UTF_8),
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

                assertThat(actualResponse.getHeaders().getContentType(), equalTo(new MediaType("text", "html", UTF_8)));
                assertNotCacheable(actualResponse);
            }

            {
                TEST_LOCALE.set(Locale.ENGLISH);
                // index.html with fallback
                HttpServletRequest request = buildRequest("/app/nextfallback/test");
                ResponseEntity<byte[]> actualResponse = cache.render(request);

                assertThat(new String(actualResponse.getBody(), UTF_8),
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

                assertThat(actualResponse.getHeaders().getContentType(), equalTo(new MediaType("text", "html", UTF_8)));
                assertNotCacheable(actualResponse);
            }

            {
                TEST_LOCALE.set(Locale.ENGLISH);
                // index.html with fallback
                HttpServletRequest request = buildRequest("/app/nextfallback/blub");
                ResponseEntity<byte[]> actualResponse = cache.render(request);

                assertThat(new String(actualResponse.getBody(), UTF_8),
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

                assertThat(actualResponse.getHeaders().getContentType(), equalTo(new MediaType("text", "html", UTF_8)));
                assertNotCacheable(actualResponse);
            }

            {
                TEST_LOCALE.set(Locale.ENGLISH);
                // index.html with fallback
                HttpServletRequest request = buildRequest("/app/lastfallback/blub");
                ResponseEntity<byte[]> actualResponse = cache.render(request);

                assertThat(new String(actualResponse.getBody(), UTF_8),
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

                assertThat(actualResponse.getHeaders().getContentType(), equalTo(new MediaType("text", "html", UTF_8)));
                assertNotCacheable(actualResponse);
            }

            {
                TEST_LOCALE.set(Locale.ENGLISH);
                // index.html with fallback
                HttpServletRequest request = buildRequest("/app/lastfallback/blub/");
                ResponseEntity<byte[]> actualResponse = cache.render(request);

                assertThat(new String(actualResponse.getBody(), UTF_8),
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

                assertThat(actualResponse.getHeaders().getContentType(), equalTo(new MediaType("text", "html", UTF_8)));
                assertNotCacheable(actualResponse);
            }

            {
                TEST_LOCALE.set(Locale.ENGLISH);
                // index.html with fallback
                HttpServletRequest request = buildRequest("/app/lastfallback/blub/test");
                ResponseEntity<byte[]> actualResponse = cache.render(request);

                assertThat(new String(actualResponse.getBody(), UTF_8),
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

                assertThat(actualResponse.getHeaders().getContentType(), equalTo(new MediaType("text", "html", UTF_8)));
                assertNotCacheable(actualResponse);
            }

            {
                TEST_LOCALE.set(Locale.ENGLISH);
                // test.js
                HttpServletRequest request = buildRequest("/app/test.js");
                ResponseEntity<byte[]> actualResponse = cache.render(request);

                assertThat(new String(actualResponse.getBody(), UTF_8), equalTo("//#message:unknown#"));

                assertThat(actualResponse.getHeaders().getContentType(),
                    equalTo(new MediaType("application", "javascript", UTF_8)));
                assertCacheable(actualResponse);
            }

            {
                TEST_LOCALE.set(Locale.ENGLISH);
                // test.js with context path
                HttpServletRequest request = buildRequest("/context/app/test.js", "/context");
                ResponseEntity<byte[]> actualResponse = cache.render(request);

                assertThat(new String(actualResponse.getBody(), UTF_8), equalTo("//#message:unknown#"));

                assertThat(actualResponse.getHeaders().getContentType(),
                    equalTo(new MediaType("application", "javascript", UTF_8)));
                assertCacheable(actualResponse);
            }
        }
        finally
        {
            TEST_LOCALE.remove();
        }
    }

    @Test
    public void testRenderingInDevMode()
    {
        StaticFolderRegistry registry =
            buildRegistry(false, false, "/app", "classpath:io/github/furti/spring/web/extended/staticfolder/app/",
                "/indexfallback", "/indexfallback/", "/nextfallback/*", "/lastfallback/**");

        StaticFolderCache cache = new StaticFolderCache(registry, buildScanners(), buildMimeTypeHandler(),
            buildTemplateFactory(), buildTemplateContextFactory(), buildResourceTypeRegistry(), buildAppInfo(false),
            buildCompressionManager());
        cache.initialize();

        try
        {
            {
                TEST_LOCALE.set(Locale.ENGLISH);
                // index.html default
                HttpServletRequest request = buildRequest("/app");
                ResponseEntity<byte[]> actualResponse = cache.render(request);

                assertThat(new String(actualResponse.getBody(), UTF_8),
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

                assertThat(actualResponse.getHeaders().getContentType(), equalTo(new MediaType("text", "html", UTF_8)));
                assertNotCacheable(actualResponse);
            }

            {
                TEST_LOCALE.set(Locale.ENGLISH);
                // test.js with context path
                HttpServletRequest request = buildRequest("/context/app/test.js", "/context");
                ResponseEntity<byte[]> actualResponse = cache.render(request);

                assertThat(new String(actualResponse.getBody(), UTF_8), equalTo("//#message:unknown#"));

                assertThat(actualResponse.getHeaders().getContentType(),
                    equalTo(new MediaType("application", "javascript", UTF_8)));
                assertNotCacheable(actualResponse);
            }
        }
        finally
        {
            TEST_LOCALE.remove();
        }
    }

    @Test
    public void testRenderingWithDifferentCompressionTypesInProdMode()
    {
        StaticFolderRegistry registry =
            buildRegistry(false, true, "/app", "classpath:io/github/furti/spring/web/extended/staticfolder/app/",
                "/indexfallback", "/indexfallback/", "/nextfallback/*", "/lastfallback/**");

        StaticFolderCache cache = new StaticFolderCache(registry, buildScanners(), buildMimeTypeHandler(),
            buildTemplateFactory(), buildTemplateContextFactory(), buildResourceTypeRegistry(), buildAppInfo(true),
            buildCompressionManager());
        cache.initialize();

        try
        {
            {
                TEST_LOCALE.set(Locale.ENGLISH);
                // index.html default no compression
                HttpServletRequest request = buildRequest("/app");
                ResponseEntity<byte[]> actualResponse = cache.render(request);

                assertThat(new String(actualResponse.getBody(), UTF_8),
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

                assertThat(actualResponse.getHeaders().getContentType(), equalTo(new MediaType("text", "html", UTF_8)));
                assertThat(actualResponse.getHeaders().getFirst(HttpHeaders.CONTENT_ENCODING), nullValue());
                assertNotCacheable(actualResponse);
            }

            {
                TEST_LOCALE.set(Locale.ENGLISH);
                // index.html gzip compression but content to small to compress
                HttpServletRequest request = buildRequestWithCompression("/app", "gzip");
                ResponseEntity<byte[]> actualResponse = cache.render(request);

                assertThat(new String(actualResponse.getBody(), UTF_8),
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

                assertThat(actualResponse.getHeaders().getContentType(), equalTo(new MediaType("text", "html", UTF_8)));
                assertThat(actualResponse.getHeaders().getFirst(HttpHeaders.CONTENT_ENCODING), nullValue());
                assertNotCacheable(actualResponse);
            }

            {
                TEST_LOCALE.set(Locale.ENGLISH);
                // big.js with supported compression. File is big enough to be compressed
                HttpServletRequest request = buildRequestWithCompression("/app/big.js", "gzip");
                ResponseEntity<byte[]> actualResponse = cache.render(request);

                byte[] unzipped = gunzip(actualResponse.getBody());

                assertThat(new String(unzipped, UTF_8),
                    equalTo("function doSomething() {"
                        + lineSeparator
                        + "console.log('Data: ABCDEFGHIJKLMNOPQRST');"
                        + lineSeparator
                        + "console.log('Data: abcdefghijklmnopqrst');"
                        + lineSeparator
                        + "console.log('Data: ABCDEFGHIJKLMNOPQRST');"
                        + lineSeparator
                        + "console.log('Data: abcdefghijklmnopqrst');"
                        + lineSeparator
                        + "console.log('Data: ABCDEFGHIJKLMNOPQRST');"
                        + lineSeparator
                        + "console.log('Data: abcdefghijklmnopqrst');"
                        + lineSeparator
                        + "console.log('Data: ABCDEFGHIJKLMNOPQRST');"
                        + lineSeparator
                        + "console.log('Data: abcdefghijklmnopqrst');"
                        + lineSeparator
                        + "console.log('Data: ABCDEFGHIJKLMNOPQRST');"
                        + lineSeparator
                        + "console.log('Data: ABCDEFGHIJKLMNOPQRST');"
                        + lineSeparator
                        + "console.log('Data: abcdefghijklmnopqrst');"
                        + lineSeparator
                        + "console.log('Data: ABCDEFGHIJKLMNOPQRST');"
                        + lineSeparator
                        + "console.log('Data: abcdefghijklmnopqrst');"
                        + lineSeparator
                        + "console.log('Data: ABCDEFGHIJKLMNOPQRST');"
                        + lineSeparator
                        + "console.log('Data: abcdefghijklmnopqrst');"
                        + lineSeparator
                        + "console.log('Data: ABCDEFGHIJKLMNOPQRST');"
                        + lineSeparator
                        + "console.log('Data: abcdefghijklmnopqrst');"
                        + lineSeparator
                        + "console.log('Data: ABCDEFGHIJKLMNOPQRST');"
                        + lineSeparator
                        + "console.log('Data: ABCDEFGHIJKLMNOPQRST');"
                        + lineSeparator
                        + "console.log('Data: abcdefghijklmnopqrst');"
                        + lineSeparator
                        + "console.log('Data: ABCDEFGHIJKLMNOPQRST');"
                        + lineSeparator
                        + "console.log('Data: abcdefghijklmnopqrst');"
                        + lineSeparator
                        + "console.log('Data: ABCDEFGHIJKLMNOPQRST');"
                        + lineSeparator
                        + "console.log('Data: abcdefghijklmnopqrst');"
                        + lineSeparator
                        + "console.log('Data: ABCDEFGHIJKLMNOPQRST');"
                        + lineSeparator
                        + "console.log('Data: abcdefghijklmnopqrst');"
                        + lineSeparator
                        + "console.log('Data: ABCDEFGHIJKLMNOPQRST');"
                        + lineSeparator
                        + "}"));

                assertThat(actualResponse.getHeaders().getContentType(),
                    equalTo(new MediaType("application", "javascript", UTF_8)));
                assertThat(actualResponse.getHeaders().getFirst(HttpHeaders.CONTENT_ENCODING), equalTo("gzip"));
                assertCacheable(actualResponse);
            }

            {
                TEST_LOCALE.set(Locale.ENGLISH);
                // big.nocompress with supported compression. File is big enough but the mimetype is not compressable
                // So no compression should be performed
                HttpServletRequest request = buildRequestWithCompression("/app/big.nocompress", "gzip");
                ResponseEntity<byte[]> actualResponse = cache.render(request);

                assertThat(new String(actualResponse.getBody(), UTF_8),
                    equalTo("function doSomething() {"
                        + lineSeparator
                        + "console.log('Data: ABCDEFGHIJKLMNOPQRST');"
                        + lineSeparator
                        + "console.log('Data: abcdefghijklmnopqrst');"
                        + lineSeparator
                        + "console.log('Data: ABCDEFGHIJKLMNOPQRST');"
                        + lineSeparator
                        + "console.log('Data: abcdefghijklmnopqrst');"
                        + lineSeparator
                        + "console.log('Data: ABCDEFGHIJKLMNOPQRST');"
                        + lineSeparator
                        + "console.log('Data: abcdefghijklmnopqrst');"
                        + lineSeparator
                        + "console.log('Data: ABCDEFGHIJKLMNOPQRST');"
                        + lineSeparator
                        + "console.log('Data: abcdefghijklmnopqrst');"
                        + lineSeparator
                        + "console.log('Data: ABCDEFGHIJKLMNOPQRST');"
                        + lineSeparator
                        + "console.log('Data: ABCDEFGHIJKLMNOPQRST');"
                        + lineSeparator
                        + "console.log('Data: abcdefghijklmnopqrst');"
                        + lineSeparator
                        + "console.log('Data: ABCDEFGHIJKLMNOPQRST');"
                        + lineSeparator
                        + "console.log('Data: abcdefghijklmnopqrst');"
                        + lineSeparator
                        + "console.log('Data: ABCDEFGHIJKLMNOPQRST');"
                        + lineSeparator
                        + "console.log('Data: abcdefghijklmnopqrst');"
                        + lineSeparator
                        + "console.log('Data: ABCDEFGHIJKLMNOPQRST');"
                        + lineSeparator
                        + "console.log('Data: abcdefghijklmnopqrst');"
                        + lineSeparator
                        + "console.log('Data: ABCDEFGHIJKLMNOPQRST');"
                        + lineSeparator
                        + "console.log('Data: ABCDEFGHIJKLMNOPQRST');"
                        + lineSeparator
                        + "console.log('Data: abcdefghijklmnopqrst');"
                        + lineSeparator
                        + "console.log('Data: ABCDEFGHIJKLMNOPQRST');"
                        + lineSeparator
                        + "console.log('Data: abcdefghijklmnopqrst');"
                        + lineSeparator
                        + "console.log('Data: ABCDEFGHIJKLMNOPQRST');"
                        + lineSeparator
                        + "console.log('Data: abcdefghijklmnopqrst');"
                        + lineSeparator
                        + "console.log('Data: ABCDEFGHIJKLMNOPQRST');"
                        + lineSeparator
                        + "console.log('Data: abcdefghijklmnopqrst');"
                        + lineSeparator
                        + "console.log('Data: ABCDEFGHIJKLMNOPQRST');"
                        + lineSeparator
                        + "}"));

                assertThat(actualResponse.getHeaders().getContentType(),
                    equalTo(new MediaType("test", "nocompress", UTF_8)));
                assertThat(actualResponse.getHeaders().getFirst(HttpHeaders.CONTENT_ENCODING), nullValue());
                assertNotCacheable(actualResponse);
            }
        }
        finally
        {
            TEST_LOCALE.remove();
        }
    }

    private byte[] gunzip(byte[] zippedBytes)
    {
        try (GzipCompressorInputStream input = new GzipCompressorInputStream(new ByteArrayInputStream(zippedBytes)))
        {
            return IOUtils.toByteArray(input);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error unzipping bytes " + zippedBytes, e);
        }
    }

    private void assertCacheable(ResponseEntity<byte[]> actualResponse)
    {
        assertThat(actualResponse.getHeaders().getCacheControl(), equalTo("public, max-age=86400, must-revalidate"));
        assertThat(actualResponse.getHeaders().getPragma(), equalTo("cache"));
    }

    private void assertNotCacheable(ResponseEntity<byte[]> actualResponse)
    {
        assertThat(actualResponse.getHeaders().getCacheControl(), equalTo("no-store, max-age=0, must-revalidate"));
        assertThat(actualResponse.getHeaders().getPragma(), equalTo("no-cache"));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testMissingFolder()
    {
        StaticFolderRegistry registry =
            buildRegistry(false, true, "/app", "classpath:io/github/furti/spring/web/extended/staticfolder/app/");

        StaticFolderCache cache = new StaticFolderCache(registry, buildScanners(), buildMimeTypeHandler(),
            buildTemplateFactory(), buildTemplateContextFactory(), buildResourceTypeRegistry(), buildAppInfo(true),
            buildCompressionManager());
        cache.initialize();

        HttpServletRequest request = buildRequest("/something");
        cache.render(request);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testMissingFallback()
    {
        StaticFolderRegistry registry = buildRegistry(false, true, "/app",
            "classpath:io/github/furti/spring/web/extended/staticfolder/app/", "/fallback/*");

        StaticFolderCache cache = new StaticFolderCache(registry, buildScanners(), buildMimeTypeHandler(),
            buildTemplateFactory(), buildTemplateContextFactory(), buildResourceTypeRegistry(), buildAppInfo(true),
            buildCompressionManager());
        cache.initialize();

        HttpServletRequest request = buildRequest("/fallback/test/all");
        cache.render(request);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testMissingResource()
    {
        StaticFolderRegistry registry =
            buildRegistry(false, true, "/app", "classpath:io/github/furti/spring/web/extended/staticfolder/app/");

        StaticFolderCache cache = new StaticFolderCache(registry, buildScanners(), buildMimeTypeHandler(),
            buildTemplateFactory(), buildTemplateContextFactory(), buildResourceTypeRegistry(), buildAppInfo(true),
            buildCompressionManager());
        cache.initialize();

        HttpServletRequest request = buildRequest("/app/missing.js");
        cache.render(request);
    }

    private ApplicationInfo buildAppInfo(boolean productionMode)
    {
        return new DefaultApplicationInfo().productionMode(productionMode);
    }

    private ResourceTypeRegistry buildResourceTypeRegistry()
    {
        DefaultResourceTypeRegistry registry = new DefaultResourceTypeRegistry();

        registry.resourceTypeByMimeType("application/javascript", ResourceType.TEMPLATE);
        registry.resourceTypeByMimeType("text/html", ResourceType.TEMPLATE);
        registry.resourceTypeByMimeType("test/nocompress", ResourceType.TEMPLATE);

        return registry;
    }

    private MimeTypeHandler buildMimeTypeHandler()
    {
        Map<String, String> mimeTypes = new HashMap<>();
        mimeTypes.put(".js", "application/javascript");
        mimeTypes.put(".html", "text/html");
        mimeTypes.put(".nocompress", "test/nocompress");

        Set<MimeType> cacheable = new HashSet<>();
        cacheable.add(MimeType.valueOf("application/javascript"));

        return new MimeTypeHandler(Mockito.mock(ServletContext.class), mimeTypes, cacheable);
    }

    private ResourceScanners buildScanners()
    {
        Map<String, ResourceScanner> scanners = new HashMap<>();
        scanners.put("classpath", new ClasspathResourceScanner());

        return new ResourceScanners(scanners);
    }

    private StaticFolderRegistry buildRegistry(boolean reloadMissingResources, boolean productionMode, String basePath,
        String location, String... indexFallbacks)
    {
        DefaultStaticFolderRegistry registry = new DefaultStaticFolderRegistry();

        registry.reloadOnMissingResource(reloadMissingResources);
        registry.registerFolder(basePath, location, indexFallbacks);
        registry.resourceRefreshInterval(productionMode ? 60 * 10 : 0);

        return registry;
    }

    private HttpServletRequest buildRequest(String requestURI)
    {
        return buildRequest(requestURI, "");
    }

    private HttpServletRequest buildRequest(String requestURI, String contextPath)
    {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        Mockito.when(request.getRequestURI()).thenReturn(requestURI);
        Mockito.when(request.getContextPath()).thenReturn(contextPath);

        return request;
    }

    private HttpServletRequest buildRequestWithCompression(String requestURI, String encodingType)
    {
        HttpServletRequest request = buildRequest(requestURI);

        Mockito.when(request.getHeader("Accept-Encoding")).thenReturn(encodingType);

        return request;
    }

    private TemplateContextFactory buildTemplateContextFactory()
    {
        return new DefaultTemplateContextFactory(buildMimeTypeHandler(), new ThreadLocalLocaleContext());
    }

    private TemplateFactory buildTemplateFactory()
    {
        StaticMessageSource messageSource = new StaticMessageSource();
        messageSource.addMessage("test", Locale.ENGLISH, "A very useful message");
        messageSource.addMessage("test", Locale.GERMAN, "Eine sehr nützliche Nachricht");

        ExpressionHandlerRegistry registry = new DefaultExpressionHandlerRegistry();
        registry.registerExpressionHandler(new MessageExpressionHandler(messageSource));

        return new SimpleTemplateFactory(registry, new DefaultContentEscapeHandlerRegistry(), '#', ':', '#');
    }

    private CompressionManager buildCompressionManager()
    {
        List<MimeType> supportedMimeTypes = new ArrayList<>();
        supportedMimeTypes.add(MediaType.parseMediaType("text/*"));
        supportedMimeTypes.add(MediaType.APPLICATION_JSON);
        supportedMimeTypes.add(MediaType.APPLICATION_JSON_UTF8);
        supportedMimeTypes.add(MediaType.parseMediaType("application/javascript"));

        return new DefaultCompressionManager(supportedMimeTypes);
    }

    /**
     * @author Daniel Furtlehner
     */
    private class ThreadLocalLocaleContext implements LocaleContext
    {

        @Override
        public Locale getLocale()
        {
            return TEST_LOCALE.get();
        }
    }
}
