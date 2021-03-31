package io.github.furti.spring.web.extended.util;

import static io.github.furti.spring.web.extended.util.LocaleUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Test;

public class LocaleUtilsTest
{
    private static final List<Locale> SUPPORTED =
        Arrays.asList(new Locale("de"), new Locale("de", "DE"), new Locale("en"));

    @Test
    public void closestSupportedLocaleTest()
    {
        assertThat(closestSupportedLocale(null, (Locale) null), equalTo(null));
        assertThat(closestSupportedLocale(Arrays.asList(), (Locale) null), equalTo(null));
        assertThat(closestSupportedLocale(Arrays.asList(Locale.getDefault()), (Locale) null), equalTo(null));
        assertThat(closestSupportedLocale(null, Locale.getDefault()), equalTo(null));
        assertThat(closestSupportedLocale(Arrays.asList(), Locale.getDefault()), equalTo(null));
        assertThat(closestSupportedLocale(SUPPORTED, new Locale("de")), equalTo(new Locale("de")));
        assertThat(closestSupportedLocale(SUPPORTED, new Locale("de", "DE")), equalTo(new Locale("de", "DE")));
        assertThat(closestSupportedLocale(SUPPORTED, new Locale("fr")), equalTo(null));
        assertThat(closestSupportedLocale(SUPPORTED, new Locale("en")), equalTo(new Locale("en")));
        assertThat(closestSupportedLocale(SUPPORTED, new Locale("en", "US")), equalTo(new Locale("en")));
    }

    @Test
    public void closestSupportedLocaleString()
    {
        assertThat(closestSupportedLocale(null, (String) null), equalTo(null));
        assertThat(closestSupportedLocale(Arrays.asList(), (String) null), equalTo(null));
        assertThat(closestSupportedLocale(Arrays.asList(Locale.getDefault()), (String) null), equalTo(null));
        assertThat(closestSupportedLocale(null, Locale.getDefault()), equalTo(null));
        assertThat(closestSupportedLocale(Arrays.asList(), "de-DE"), equalTo(null));
        assertThat(closestSupportedLocale(SUPPORTED, "de"), equalTo(new Locale("de")));
        assertThat(closestSupportedLocale(SUPPORTED, "de-DE"), equalTo(new Locale("de", "DE")));
        assertThat(closestSupportedLocale(SUPPORTED, "fr"), equalTo(null));
        assertThat(closestSupportedLocale(SUPPORTED, "en"), equalTo(new Locale("en")));
        assertThat(closestSupportedLocale(SUPPORTED, "en-US"), equalTo(new Locale("en")));
    }
}
