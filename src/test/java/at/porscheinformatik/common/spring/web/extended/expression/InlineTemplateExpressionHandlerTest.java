package at.porscheinformatik.common.spring.web.extended.expression;

import static org.mockito.Mockito.*;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.springframework.beans.factory.BeanFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import at.porscheinformatik.common.spring.web.extended.template.cache.html.HtmlStack;
import at.porscheinformatik.common.spring.web.extended.template.cache.html.HtmlStacks;

public class InlineTemplateExpressionHandlerTest
{

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

        MatcherAssert.assertThat(result, CoreMatchers.equalTo("This  is a \"T  E  S  T\"."));
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
