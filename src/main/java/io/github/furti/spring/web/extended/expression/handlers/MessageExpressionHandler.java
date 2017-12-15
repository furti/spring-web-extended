/**
 * 
 */
package io.github.furti.spring.web.extended.expression.handlers;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import io.github.furti.spring.web.extended.expression.BaseExpressionHandler;
import io.github.furti.spring.web.extended.template.TemplateContext;

/**
 * @author Daniel Furtlehner
 */
public class MessageExpressionHandler extends BaseExpressionHandler
{
    private final MessageSource messageSource;

    public MessageExpressionHandler(MessageSource messageSource)
    {
        super("message");

        this.messageSource = messageSource;
    }

    @Override
    protected String doProcess(String value, TemplateContext templateContext)
    {
        try
        {
            //TODO: use locale from templatecontext
            return messageSource.getMessage(value, null, Locale.getDefault());
        }
        catch (NoSuchMessageException ex)
        {
            return "{Message \"" + value + "\" not found}";
        }
    }
}