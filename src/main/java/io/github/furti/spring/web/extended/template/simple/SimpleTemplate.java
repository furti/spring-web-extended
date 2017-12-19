/**
 * 
 */
package io.github.furti.spring.web.extended.template.simple;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;

import io.github.furti.spring.web.extended.expression.ExpressionHandler;
import io.github.furti.spring.web.extended.expression.ExpressionHandlerRegistry;
import io.github.furti.spring.web.extended.template.CacheableTemplate;
import io.github.furti.spring.web.extended.template.TemplateContext;

/**
 * @author Daniel Furtlehner
 */
public class SimpleTemplate extends CacheableTemplate
{
    private final TemplateContext context;
    private final ExpressionHandlerRegistry expressionHandlers;
    private final ContentEscapeHandlerRegistry escapeHandlers;
    private final char expressionStart;
    private final char expressionEnd;
    private final char expressionDelimiter;

    public SimpleTemplate(Resource resource, TemplateContext context, Charset charset,
        ExpressionHandlerRegistry expressionHandlers, ContentEscapeHandlerRegistry escapeHandlers)
    {
        super(resource, charset);

        this.context = context;
        this.expressionHandlers = expressionHandlers;
        this.escapeHandlers = escapeHandlers;

        //TODO: make this configurable
        this.expressionStart = '§';
        this.expressionDelimiter = '.';
        this.expressionEnd = '§';
    }

    @Override
    protected String doRender() throws IOException
    {
        List<TemplatePart> parts = parseTemplate();

        if (parts.size() == 0)
        {
            return "";
        }

        StringBuilder content = new StringBuilder();

        for (TemplatePart part : parts)
        {
            content.append(part.render(context));
        }

        return content.toString();
    }

    private List<TemplatePart> parseTemplate() throws IOException
    {
        List<TemplatePart> parts = new ArrayList<TemplatePart>();

        String templateContent = loadTemplate();
        int templateLength = templateContent.length();

        StringBuilder staticPart = new StringBuilder();
        StringBuilder expression = new StringBuilder();
        boolean insideExpression = false;

        for (int index = 0; index < templateLength; index++)
        {
            char actualChar = templateContent.charAt(index);

            if (expressionStart == actualChar && !insideExpression)
            {
                insideExpression = true;

                if (staticPart.length() > 0)
                {
                    parts.add(new StaticTemplatePart(staticPart.toString()));

                    staticPart = new StringBuilder();
                }

                expression.append(expressionStart);
            }
            else if (expressionEnd == actualChar && insideExpression)
            {
                insideExpression = false;

                expression.append(expressionEnd);

                String expressionAsString = expression.toString();

                // Parse the expression
                HandlerAndValue handlerAndValue = parseExpression(expressionAsString);

                if (invalidExpression(handlerAndValue))
                {
                    /*
                     * We have an invalid expression here. We need to check if expression start and expression end 
                     * are the same characters. If so we have to continue with a new expression at the end of the current expression. 
                     * Because maybe we encountered something thta looks like an expression but the actual expression starts now.
                     */
                    if (expressionStart == expressionEnd)
                    {
                        parts.add(new StaticTemplatePart(expression.substring(0, expression.length() - 1)));

                        expression = new StringBuilder();
                        expression.append(expressionStart);

                        insideExpression = true;

                    }
                    else
                    {
                        parts.add(new StaticTemplatePart(expressionAsString));

                        expression = new StringBuilder();
                        insideExpression = false;
                    }
                }
                else
                {
                    parts.add(new ExpressionPart(expressionAsString, handlerAndValue.handler, handlerAndValue.value,
                        escapeHandlers.getEscapeHandlerForMimeType(context.getMimeType())));

                    expression = new StringBuilder();
                    insideExpression = false;
                }
            }
            else if (expressionStart == actualChar)
            {
                //Maybe an unclosed expression. Add the expression as static part and restart an expression.
                parts.add(new StaticTemplatePart(expression.toString()));

                insideExpression = true;

                expression = new StringBuilder();
                expression.append(expressionStart);
            }
            else if (insideExpression)
            {
                expression.append(actualChar);
            }
            else
            {
                // No expression. Therefore add it as static content.
                staticPart.append(actualChar);
            }
        }

        if (staticPart.length() > 0)
        {
            parts.add(new StaticTemplatePart(staticPart.toString()));
        }

        if (expression.length() > 0)
        {
            // Maybe an unclosed expression at the end. Simple use it as it is.
            parts.add(new StaticTemplatePart(expression.toString()));
        }

        return parts;
    }

    private boolean invalidExpression(HandlerAndValue handlerAndValue)
    {
        // No expression handler found.
        if (handlerAndValue.handler == null)
        {
            return true;
        }

        // The expression handler needs an expression but none was provided. Maybe only something that looks like an expression.
        if (handlerAndValue.handler.isValueRequired() && handlerAndValue.value == null)
        {
            return true;
        }

        return false;
    }

    private HandlerAndValue parseExpression(String expression) throws IOException
    {
        String[] handlernameAndValue = splitExpression(expression);

        ExpressionHandler expressionHandler = expressionHandlers.getExpressionHandler(handlernameAndValue[0]);

        return new HandlerAndValue(expressionHandler, handlernameAndValue[1]);
    }

    private String[] splitExpression(String expression) throws IOException
    {
        // Skip the expression start and end characters
        expression = expression.substring(1, expression.length() - 1);

        int splitIndex = expression.indexOf(expressionDelimiter);

        if (splitIndex == -1)
        {
            // No value in the expression

            return new String[]{expression, null};
        }

        return new String[]{expression.substring(0, splitIndex), expression.substring(splitIndex + 1)};
    }

    private static class HandlerAndValue
    {
        private ExpressionHandler handler;
        private String value;

        public HandlerAndValue(ExpressionHandler handler, String value)
        {
            super();
            this.handler = handler;
            this.value = value;
        }
    }

}
