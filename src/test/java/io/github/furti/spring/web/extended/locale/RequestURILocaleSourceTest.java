package io.github.furti.spring.web.extended.locale;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import io.github.furti.spring.web.extended.config.DefaultApplicationConfiguration;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class RequestURILocaleSourceTest {

    @Test
    public void getLocale() {
        performGetLocale(null, null, null);
        performGetLocale("", null, null);
        performGetLocale("", "", null);
        performGetLocale("/", null, null);
        performGetLocale("/", "/", null);
        performGetLocale("/test", null, null);
        performGetLocale("/abc", "/test", null);
        performGetLocale("/de", "/test", new Locale("de"));
        performGetLocale("/de-AT", "/test", new Locale("de", "AT"));
        performGetLocale("/de-DE", "/test", new Locale("de"));
        performGetLocale("/xy-AB", "/test", null);
    }

    private void performGetLocale(String servletPath, String pathInfo, Locale expected) {
        DefaultApplicationConfiguration appConfig = new DefaultApplicationConfiguration();
        appConfig.addLocale("de");
        appConfig.addLocale("de", "AT");
        appConfig.addLocale("en");

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getServletPath()).thenReturn(servletPath);
        Mockito.when(request.getPathInfo()).thenReturn(pathInfo);
        RequestURILocaleSource source = new RequestURILocaleSource();
        source.setAppConfig(appConfig);

        Locale actual = source.getLocale(request, null);

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void getPossibleLocale() {
        performGetPossibleLocale(null, null, "");
        performGetPossibleLocale("", null, "");
        performGetPossibleLocale("abc", null, "abc");
        performGetPossibleLocale("/", null, null);
        performGetPossibleLocale("/test", null, "test");
        performGetPossibleLocale("/abc", "/test", "abc");
        performGetPossibleLocale("/de", "/test", "de");
        performGetPossibleLocale("de_AT", "/test", "de_AT");
        performGetPossibleLocale("/xy_AB", "/test/", "xy_AB");
        performGetPossibleLocale("xy-AB", "/test/", "xy-AB");
    }

    private void performGetPossibleLocale(String servletPath, String pathInfo, String expected) {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getServletPath()).thenReturn(servletPath);
        Mockito.when(request.getPathInfo()).thenReturn(pathInfo);
        RequestURILocaleSource source = new RequestURILocaleSource();

        String actual = source.getPossibleLocale(request, null);

        assertThat(actual, equalTo(expected));
    }
}
