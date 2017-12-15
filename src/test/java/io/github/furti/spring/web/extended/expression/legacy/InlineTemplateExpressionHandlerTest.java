package io.github.furti.spring.web.extended.expression.legacy;

import static org.mockito.Mockito.*;

import java.util.Locale;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.springframework.beans.factory.BeanFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.furti.spring.web.extended.io.ResourceType;
import io.github.furti.spring.web.extended.template.legacy.DefaultTemplateRenderContext;
import io.github.furti.spring.web.extended.template.legacy.TemplateRenderContextHolder;
import io.github.furti.spring.web.extended.template.legacy.cache.html.HtmlStack;
import io.github.furti.spring.web.extended.template.legacy.cache.html.HtmlStacks;

public class InlineTemplateExpressionHandlerTest
{

    @BeforeClass
    public void setupFactory()
    {
        TemplateRenderContextHolder
            .setCurrentContext(new DefaultTemplateRenderContext(Locale.getDefault(), ResourceType.HTML));
    }

    @AfterClass
    public void cleanup()
    {
        TemplateRenderContextHolder.removeCurrentContext();
    }

    @Test
    public void testSimpleProcessing()
    {
        InlineTemplateExpressionHandler handler = createHandler("testtemplate.html", "Test Content");
        String result = handler.process("testTemplate");

        MatcherAssert.assertThat(result, CoreMatchers.equalTo("Test Content"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUnknownTemplate()
    {
        InlineTemplateExpressionHandler handler = createHandler("testtemplate.html", "Test Content");

        handler.process("unknownTemplate");

        Assert.fail("Expected exception");
    }

    @Test
    public void testReplace()
    {
        InlineTemplateExpressionHandler handler =
            createHandler("testtemplate.html", "\nThis\r\n\tis\t\ra\f\"T  E  S  T\".\r");
        String result = handler.process("testTemplate");

        MatcherAssert.assertThat(result, CoreMatchers.equalTo("This  is a &quot;T  E  S  T&quot;."));
    }

    private InlineTemplateExpressionHandler createHandler(String templateName, String renderedTemplate)
    {
        HtmlStack defaultStack = mock(HtmlStack.class);

        when(defaultStack.hasTemplate(templateName)).thenReturn(true);
        when(defaultStack.renderTemplate(templateName)).thenReturn(renderedTemplate);

        HtmlStacks stacks = mock(HtmlStacks.class);

        when(stacks.hasStack("")).thenReturn(true);
        when(stacks.get("")).thenReturn(defaultStack);

        BeanFactory beanFactory = mock(BeanFactory.class);

        when(beanFactory.getBean(HtmlStacks.class)).thenReturn(stacks);

        InlineTemplateExpressionHandler handler = new InlineTemplateExpressionHandler();

        handler.setBeanFactory(beanFactory);

        return handler;
    }
}
