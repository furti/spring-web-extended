/**
 * 
 */
package io.github.furti.spring.web.extended.expression.handlers;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import io.github.furti.spring.web.extended.expression.BaseExpressionHandler;
import io.github.furti.spring.web.extended.template.TemplateContext;
import io.github.furti.spring.web.extended.template.TemplateContextParameter;

/**
 * @author Daniel Furtlehner
 */
public class MessageExpressionHandler extends BaseExpressionHandler
{
    private final MessageSource messageSource;

    public MessageExpressionHandler(MessageSource messageSource)
    {
        super("message", true);

        this.messageSource = messageSource;
    }

    @Override
    protected String doProcess(String value, TemplateContext templateContext)
    {
        try
        {
            Locale locale = (Locale) templateContext.getParameter(TemplateContextParameter.LOCALE);

            return messageSource.getMessage(value, null, locale != null ? locale : Locale.getDefault());
        }
        catch (NoSuchMessageException ex)
        {
            return null;
        }
    }
}