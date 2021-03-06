package io.github.furti.spring.web.extended.messagesource;

import java.util.Set;

public interface MessageSourceConfig
{

    Integer getCacheSeconds();

    void setCacheSeconds(Integer seconds);

    Set<String> getBaseNames();

    void addBaseName(String baseName);

    void removeBaseName(String baseName);

    String getDefaultEncoding();

    void setDefaultEncoding(String defaultEncoding);

}
