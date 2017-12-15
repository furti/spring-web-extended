/**
 * 
 */
package io.github.furti.spring.web.extended.staticfolder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.MessageSource;

import io.github.furti.spring.web.extended.MessageRegistry;

/**
 * @author Daniel Furtlehner
 */
public class DefaultMessageRegistry implements MessageRegistry
{

    private final Set<String> basenames = new HashSet<>();

    private MessageSource messageSource;
    private String encoding;

    @Override
    public void setMessageSource(MessageSource messageSource)
    {
        this.messageSource = messageSource;
    }

    @Override
    public MessageSource getMessageSource()
    {
        return messageSource;
    }

    @Override
    public MessageRegistry setEncoding(String encoding)
    {
        this.encoding = encoding;

        return this;
    }

    @Override
    public String getEncoding()
    {
        return encoding;
    }

    @Override
    public Collection<String> getBasenames()
    {
        return basenames;
    }

    @Override
    public MessageRegistry addBasename(String basename)
    {
        basenames.add(basename);

        return this;
    }

    @Override
    public MessageRegistry removeBasename(String basename)
    {
        basenames.remove(basename);

        return this;
    }
}
