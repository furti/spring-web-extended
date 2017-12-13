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
package io.github.furti.spring.web.extended.template.velocity;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Locale;

import org.apache.commons.io.IOUtils;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.github.furti.spring.web.extended.io.ResourceType;
import io.github.furti.spring.web.extended.template.DefaultTemplateRenderContext;
import io.github.furti.spring.web.extended.template.Template;
import io.github.furti.spring.web.extended.template.TemplateFactory;
import io.github.furti.spring.web.extended.template.TemplateRenderContextHolder;
import io.github.furti.spring.web.extended.template.TestExpressionHandlers;
import io.github.furti.spring.web.extended.template.velocity.VelocityTemplateFactory;

public class VelocityTemplateTest
{

    private TemplateFactory factory;
    private final ResourceLoader loader = new DefaultResourceLoader();

    @BeforeClass
    public void setupFactory()
    {
        factory = new VelocityTemplateFactory();

        ((VelocityTemplateFactory) factory).setExpressionHandlers(new TestExpressionHandlers());
        ((VelocityTemplateFactory) factory).setupEngine();

        TemplateRenderContextHolder
            .setCurrentContext(new DefaultTemplateRenderContext(Locale.getDefault(), ResourceType.HTML));
    }

    @AfterClass
    public void cleanup()
    {
        TemplateRenderContextHolder.removeCurrentContext();
    }

    @Test(dataProvider = "renderTemplateData")
    public void renderTemplate(Resource resource, String templateName, ResourceType type, String expected)
        throws Exception
    {
        Template t = factory.createTemplate(resource, templateName, resource.getDescription(), type, false);

        String actual = t.render();

        Assert.assertThat(actual, CoreMatchers.equalTo(expected));
    }

    @Test(dataProvider = "renderTemplateData")
    public void performanceTest(Resource resource, String templateName, ResourceType type, String expected)
        throws Exception
    {
        Template t = factory.createTemplate(resource, templateName, resource.getDescription(), type, false);

        for (int i = 0; i < 1000; i++)
        {
            Assert.assertThat(t.render(), CoreMatchers.equalTo(expected));
        }
    }

    @DataProvider
    public Object[][] renderTemplateData()
    {
        return new Object[][]{{
            loader
                .getResource("classpath:at/porscheinformatik/common/spring/web/extended/template/velocity/Index.html"),
            "index.html",
            ResourceType.HTML,
            templateContent(loader.getResource(
                "classpath:at/porscheinformatik/common/spring/web/extended/template/velocity/Index_expected.html"))}};
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
