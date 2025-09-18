package io.github.furti.spring.web.extended.expression.legacy;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import io.github.furti.spring.web.extended.io.ResourceType;
import io.github.furti.spring.web.extended.template.legacy.DefaultTemplateRenderContext;
import io.github.furti.spring.web.extended.template.legacy.TemplateRenderContextHolder;
import io.github.furti.spring.web.extended.template.legacy.cache.html.HtmlStack;
import io.github.furti.spring.web.extended.template.legacy.cache.html.HtmlStacks;
import java.util.Locale;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;

public class InlineTemplateExpressionHandlerTest {

    @BeforeAll
    public static void setupFactory() {
        TemplateRenderContextHolder.setCurrentContext(
            new DefaultTemplateRenderContext(Locale.getDefault(), ResourceType.HTML)
        );
    }

    @AfterAll
    public static void cleanup() {
        TemplateRenderContextHolder.removeCurrentContext();
    }

    @Test
    public void testSimpleProcessing() {
        InlineTemplateExpressionHandler handler = createHandler("testtemplate.html", "Test Content");
        String result = handler.process("testTemplate");

        MatcherAssert.assertThat(result, CoreMatchers.equalTo("Test Content"));
    }

    @Test
    public void testUnknownTemplate() {
        InlineTemplateExpressionHandler handler = createHandler("testtemplate.html", "Test Content");

        assertThrows(IllegalArgumentException.class, () -> {
            handler.process("unknownTemplate");
        });
    }

    @Test
    public void testReplace() {
        InlineTemplateExpressionHandler handler = createHandler(
            "testtemplate.html",
            "\nThis\r\n\tis\t\ra\f\"T  E  S  T\".\r"
        );
        String result = handler.process("testTemplate");

        MatcherAssert.assertThat(result, CoreMatchers.equalTo("This  is a &#34;T  E  S  T&#34;."));
    }

    private InlineTemplateExpressionHandler createHandler(String templateName, String renderedTemplate) {
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
