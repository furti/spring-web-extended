package io.github.furti.spring.web.extended.expression;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.util.Assert;

public class MessageExpressionHandler extends BaseExpressionHandler
{

    private MessageSource messageSource;
    private LocaleContext locale;

    public MessageExpressionHandler()
    {
        super(true);
    }

    @Override
    public String doProcess(String value)
    {
        Assert.notNull(messageSource, "Messagesource must not be null");

        try
        {
            return messageSource.getMessage(value, null, locale.getLocale());
        }
        catch (NoSuchMessageException ex)
        {
            return "{Message \"" + value + "\" not found}";
        }
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource)
    {
        this.messageSource = messageSource;
    }

    @Autowired
    public void setLocaleContext(LocaleContext locale)
    {
        this.locale = locale;
    }
}
