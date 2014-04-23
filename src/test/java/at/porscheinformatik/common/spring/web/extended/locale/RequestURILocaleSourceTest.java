package at.porscheinformatik.common.spring.web.extended.locale;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.mockito.Mockito;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import at.porscheinformatik.common.spring.web.extended.config.DefaultApplicationConfiguration;

public class RequestURILocaleSourceTest
{

    @Test(dataProvider = "getLocaleData")
    public void getLocale(String servletPath, String pathInfo, Locale expected)
    {
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

    @Test(dataProvider = "getPossibleLocaleData")
    public void getPossibleLocale(String servletPath, String pathInfo, String expected)
    {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getServletPath()).thenReturn(servletPath);
        Mockito.when(request.getPathInfo()).thenReturn(pathInfo);
        RequestURILocaleSource source = new RequestURILocaleSource();

        String actual = source.getPossibleLocale(request, null);

        assertThat(actual, equalTo(expected));
    }

    @DataProvider
    public Object[][] getLocaleData()
    {
        return new Object[][]{
            {null, null, null},
            {"", null, null},
            {"", "", null},
            {"/", null, null},
            {"/", "/", null},
            {"/test", null, null},
            {"/abc", "/test", null},
            {"/de", "/test", new Locale("de")},
            {"/de-AT", "/test", new Locale("de", "AT")},
            {"/de-DE", "/test", new Locale("de")},
            {"/xy-AB", "/test", null}
        };
    }

    @DataProvider
    public Object[][] getPossibleLocaleData()
    {
        return new Object[][]{
            {null, null, ""},
            {"", null, ""},
            {"abc", null, "abc"},
            {"/", null, null},
            {"/test", null, "test"},
            {"/abc", "/test", "abc"},
            {"/de", "/test", "de"},
            {"de_AT", "/test", "de_AT"},
            {"/xy_AB", "/test/", "xy_AB"},
            {"xy-AB", "/test/", "xy-AB"}
        };
    }
}
