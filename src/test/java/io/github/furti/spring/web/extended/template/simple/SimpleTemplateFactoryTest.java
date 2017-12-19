/**
 * 
 */
package io.github.furti.spring.web.extended.template.simple;

import static org.hamcrest.CoreMatchers.*;

import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Assert;
import org.testng.annotations.Test;

import io.github.furti.spring.web.extended.expression.DefaultExpressionHandlerRegistry;
import io.github.furti.spring.web.extended.template.DefaultTemplateContext;
import io.github.furti.spring.web.extended.template.StringResource;
import io.github.furti.spring.web.extended.template.Template;
import io.github.furti.spring.web.extended.template.TemplateFactory;

/**
 * @author Daniel Furtlehner
 */
public class SimpleTemplateFactoryTest
{
    private static final Charset CHARSET = Charset.forName("UTF-8");
    private static final DefaultTemplateContext CONTEXT = new DefaultTemplateContext();

    @Test
    public void emptyTemplateShouldBuildEmptyString() throws IOException
    {
        Template template = buildTemplate("");

        String actualContent = template.render();

        Assert.assertThat(actualContent, equalTo(""));
    }

    @Test
    public void nullValueShouldPreserveExpressionString() throws IOException
    {
        Template template = buildTemplate("§null.test§");

        String actualContent = template.render();

        Assert.assertThat(actualContent, equalTo("§null.test§"));
    }

    @Test
    public void noValueShouldbeCallableWithoutValue() throws IOException
    {
        Template template = buildTemplate("§novalue§");

        String actualContent = template.render();

        Assert.assertThat(actualContent, equalTo("somevalue"));
    }

    @Test
    public void valueShouldbeReplaced() throws IOException
    {
        Template template = buildTemplate("§value.avalue§");

        String actualContent = template.render();

        Assert.assertThat(actualContent, equalTo("avalue"));
    }

    @Test
    public void whitespacesInValueShouldWork() throws IOException
    {
        Template template = buildTemplate("§value.another value§");

        String actualContent = template.render();

        Assert.assertThat(actualContent, equalTo("another value"));
    }

    @Test
    public void missingValueShouldPreserveExpressionString() throws IOException
    {
        Template template = buildTemplate("§value§");

        String actualContent = template.render();

        Assert.assertThat(actualContent, equalTo("§value§"));
    }

    @Test
    public void expressionStartWithoutValidExpressionShouldBePreserved() throws IOException
    {
        Template template = buildTemplate("Some Paragraph § and a §value.expression§");

        String actualContent = template.render();

        Assert.assertThat(actualContent, equalTo("Some Paragraph § and a expression"));
    }

    @Test
    public void complexTemplateStartingAndEndingWithExpression() throws IOException
    {
        Template template =
            buildTemplate("§value.Some§ Template starting\nand §value.ending§ with an §value.expression§");

        String actualContent = template.render();

        Assert.assertThat(actualContent, equalTo("Some Template starting\nand ending with an expression"));
    }

    @Test
    public void complexTemplateNotStartingAndEndingWithExpression() throws IOException
    {
        Template template = buildTemplate("Some Template §value.not§ starting\nand §value.ending§ with an expression");

        String actualContent = template.render();

        Assert.assertThat(actualContent, equalTo("Some Template not starting\nand ending with an expression"));
    }

    protected Template buildTemplate(String content) throws IOException
    {
        TemplateFactory factory = buildTemplateFactory();
        Template template = factory.createTemplate(new StringResource(content), CONTEXT, CHARSET);
        template.refreshIfNeeded();

        return template;
    }

    private TemplateFactory buildTemplateFactory()
    {
        DefaultExpressionHandlerRegistry expressionHandlers = new DefaultExpressionHandlerRegistry();
        expressionHandlers.registerExpressionHandler(new NullExpressionHandler());
        expressionHandlers.registerExpressionHandler(new NoValueExpressionHandler());
        expressionHandlers.registerExpressionHandler(new ValueReturningExpressionHandler());

        return new SimpleTemplateFactory(expressionHandlers);
    }

}
