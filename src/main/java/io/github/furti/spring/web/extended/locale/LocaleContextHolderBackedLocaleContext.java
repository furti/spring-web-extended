package io.github.furti.spring.web.extended.locale;

import java.util.Locale;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @author Daniel Furtlehner
 *
 */
public class LocaleContextHolderBackedLocaleContext implements LocaleContext {

    @Override
    public Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }
}
