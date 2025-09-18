package io.github.furti.spring.web.extended.io;

import java.util.Locale;
import org.springframework.core.io.Resource;

public interface LocalizedResourceLoader {
    Resource getResource(String resource, Locale locale);
}
