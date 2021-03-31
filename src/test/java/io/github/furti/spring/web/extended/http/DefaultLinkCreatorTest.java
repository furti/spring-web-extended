package io.github.furti.spring.web.extended.http;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;

import io.github.furti.spring.web.extended.config.DefaultApplicationConfiguration;

public class DefaultLinkCreatorTest
{

    @Test
    public void createLink()
    {
        performCreateLink(null, null, null, null, null, null);
        performCreateLink(null, new Locale("de"), null, null, null, null);
        performCreateLink(null, new Locale("de"), "123", null, null, null);
        performCreateLink(new String[]{"test"}, new Locale("en"), null, null, null, "/en/test");
        performCreateLink(new String[]{"test"}, new Locale("en"), null, "prefix", null, "prefix/en/test");
        performCreateLink(new String[]{"test"}, new Locale("en"), null, "/prefix", "suffix", "/prefix/en/test/suffix");
        performCreateLink(new String[]{"test"}, new Locale("en"), null, null, "suffix", "/en/test/suffix");
        performCreateLink(new String[]{"test"}, new Locale("en"), null, "prefix/", "suffix", "prefix/en/test/suffix");
        performCreateLink(new String[]{"test"}, new Locale("de"), null, null, null, "/de/test");
        performCreateLink(new String[]{"test"}, new Locale("de", "AT"), null, null, null, "/de-AT/test");
        performCreateLink(new String[]{"test"}, new Locale("de"), "123", null, null, "/de/123/test");
        performCreateLink(new String[]{"test", "abc", "def"}, new Locale("de"), "123", null, null,
            "/de/123/test/abc/def");
        performCreateLink(new String[]{"test", "abc", "def"}, new Locale("de"), null, null, null, "/de/test/abc/def");
        performCreateLink(new String[]{"test", "/abc", "def"}, new Locale("de"), null, null, null, "/de/test/abc/def");
    }

    private void performCreateLink(String[] parts, Locale locale, String appVersion, String prefix, String suffix,
        String expected)
    {
        DefaultLinkCreator creator = setupCreatorAndLocale(appVersion, locale, prefix, suffix);

        String actual = creator.createLink(parts);

        assertThat(actual, equalTo(expected));
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
