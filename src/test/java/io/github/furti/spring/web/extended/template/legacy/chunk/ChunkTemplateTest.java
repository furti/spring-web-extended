/**
 * Copyright 2014 Daniel Furtlehner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.furti.spring.web.extended.template.legacy.chunk;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Locale;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import io.github.furti.spring.web.extended.io.ResourceType;
import io.github.furti.spring.web.extended.template.legacy.DefaultTemplateRenderContext;
import io.github.furti.spring.web.extended.template.legacy.Template;
import io.github.furti.spring.web.extended.template.legacy.TemplateFactory;
import io.github.furti.spring.web.extended.template.legacy.TemplateRenderContextHolder;
import io.github.furti.spring.web.extended.template.legacy.TestExpressionHandlers;

public class ChunkTemplateTest
{

    private static TemplateFactory factory;
    private final ResourceLoader loader = new DefaultResourceLoader();

    @BeforeAll
    public static void setupFactory()
    {
        factory = new ChunkTemplateFactory();

        ((ChunkTemplateFactory) factory).setExpressionHandlers(new TestExpressionHandlers());
        ((ChunkTemplateFactory) factory).buildTheme();

        TemplateRenderContextHolder
            .setCurrentContext(new DefaultTemplateRenderContext(Locale.getDefault(), ResourceType.HTML));
    }

    @AfterAll
    public static void cleanup()
    {
        TemplateRenderContextHolder.removeCurrentContext();
    }

    @Test
    public void renderTemplate() throws Exception
    {
        Resource resource =
            loader.getResource("classpath:io/github/furti/spring/web/extended/template/chunk/Index.html");
        String templateName = "index.html";
        ResourceType type = ResourceType.HTML;
        String expected = templateContent(
            loader.getResource("classpath:io/github/furti/spring/web/extended/template/chunk/Index_expected.html"));

        Template t = factory.createTemplate(resource, templateName, resource.getDescription(), type, false);

        String actual = t.render();

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void performanceTest() throws Exception
    {
        Resource resource =
            loader.getResource("classpath:io/github/furti/spring/web/extended/template/chunk/Index.html");
        String templateName = "index.html";
        ResourceType type = ResourceType.HTML;
        String expected = templateContent(
            loader.getResource("classpath:io/github/furti/spring/web/extended/template/chunk/Index_expected.html"));

        Template t = factory.createTemplate(resource, templateName, resource.getDescription(), type, false);

        for (int i = 0; i < 1000; i++)
        {
            assertThat(t.render(), equalTo(expected));
        }
    }

    private String templateContent(Resource resource)
    {
        try (Reader in = new InputStreamReader(resource.getInputStream(), Charset.forName("UTF-8")))
        {
            return IOUtils.toString(in);
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex);
        }
    }
}
