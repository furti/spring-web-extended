package io.github.furti.spring.web.extended.locale;

import java.util.List;
import java.util.Locale;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import io.github.furti.spring.web.extended.config.ApplicationConfiguration;
import io.github.furti.spring.web.extended.util.LocaleUtils;

public abstract class AbstractLocaleSource implements LocaleSource
{
    private List<Locale> availableLocales;

    @Override
    public Locale getLocale(HttpServletRequest request, HttpServletResponse response)
    {
        String possibleLocale = getPossibleLocale(request, response);

        return checkLocale(possibleLocale);
    }

    private Locale checkLocale(String possibleLocale)
    {
        return LocaleUtils.closestSupportedLocale(availableLocales, possibleLocale);
    }

    protected abstract String getPossibleLocale(HttpServletRequest request, HttpServletResponse response);

    @Autowired
    public void setAppConfig(ApplicationConfiguration appConfig)
    {
        if (appConfig != null)
        {
            availableLocales = appConfig.getSupportedLocales();
        }
    }
}
