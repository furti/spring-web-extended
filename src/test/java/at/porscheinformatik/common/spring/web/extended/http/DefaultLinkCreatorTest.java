package at.porscheinformatik.common.spring.web.extended.http;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import at.porscheinformatik.common.spring.web.extended.config.DefaultApplicationConfiguration;

public class DefaultLinkCreatorTest
{

    @Test(dataProvider = "createLinkData")
    public void createLink(String[] parts, Locale locale, String appVersion, String prefix, String suffix,
        String expected)
    {
        DefaultLinkCreator creator = setupCreatorAndLocale(appVersion, locale, prefix, suffix);

        String actual = creator.createLink(parts);

        assertThat(actual, equalTo(expected));
    }

    @DataProvider
    public Object[][] createLinkData()
    {
        return new Object[][]{
            {null, null, null, null, null, null},
            {null, new Locale("de"), null, null, null, null},
            {null, new Locale("de"), "123", null, null, null},
            {new String[]{"test"}, new Locale("en"), null, null, null, "/en/test"},
            {new String[]{"test"}, new Locale("en"), null, "prefix", null, "prefix/en/test"},
            {new String[]{"test"}, new Locale("en"), null, "/prefix", "suffix", "/prefix/en/test/suffix"},
            {new String[]{"test"}, new Locale("en"), null, null, "suffix", "/en/test/suffix"},
            {new String[]{"test"}, new Locale("en"), null, "prefix/", "suffix", "prefix/en/test/suffix"},
            {new String[]{"test"}, new Locale("de"), null, null, null, "/de/test"},
            {new String[]{"test"}, new Locale("de", "AT"), null, null, null, "/de-AT/test"},
            {new String[]{"test"}, new Locale("de"), "123", null, null, "/de/123/test"},
            {new String[]{"test", "abc", "def"}, new Locale("de"), "123", null, null, "/de/123/test/abc/def"},
            {new String[]{"test", "abc", "def"}, new Locale("de"), null, null, null, "/de/test/abc/def"},
            {new String[]{"test", "/abc", "def"}, new Locale("de"), null, null, null, "/de/test/abc/def"}};
    }

    private DefaultLinkCreator setupCreatorAndLocale(String appVersion, Locale locale, String prefix, String suffix)
    {
        DefaultLinkCreator creator = new TestLinkCreator(prefix, suffix);
        DefaultApplicationConfiguration appConfig = new DefaultApplicationConfiguration();
        appConfig.setVersion(appVersion);
        creator.setAppConfig(appConfig);

        LocaleContextHolder.setLocale(locale);

        return creator;
    }

    private static class TestLinkCreator extends DefaultLinkCreator
    {
        private final String prefix, suffix;

        public TestLinkCreator(String prefix, String suffix)
        {
            super();
            this.prefix = prefix;
            this.suffix = suffix;
        }

        @Override
        protected void prefix(StringBuilder url)
        {
            if (prefix != null)
            {
                url.append(prefix);
            }
        }

        @Override
        protected void suffix(StringBuilder url)
        {
            if (suffix != null)
            {
                url.append("/").append(suffix);
            }
        }
    }
}
