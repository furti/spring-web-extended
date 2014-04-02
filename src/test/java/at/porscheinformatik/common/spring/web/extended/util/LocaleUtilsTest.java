package at.porscheinformatik.common.spring.web.extended.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LocaleUtilsTest
{
	private static final List<Locale> SUPPORTED = Arrays.asList(
			new Locale("de"), new Locale("de", "DE"), new Locale("en"));

	@Test(dataProvider = "localeData")
	public void closestSupportedLocale(Locale locale,
			List<Locale> supportedLocales, Locale expected)
	{
		Locale actual = LocaleUtils.closestSupportedLocale(supportedLocales,
				locale);

		assertThat(actual, equalTo(expected));
	}

	@Test(dataProvider = "stringData")
	public void closestSupportedLocaleString(String languageTag,
			List<Locale> supportedLocales, Locale expected)
	{
		Locale actual = LocaleUtils.closestSupportedLocale(supportedLocales,
				languageTag);

		assertThat(actual, equalTo(expected));
	}

	@DataProvider
	public Object[][] localeData()
	{
		return new Object[][] {
				{ null, null, null },
				{ null, Arrays.asList(), null },
				{ null, Arrays.asList(Locale.getDefault()), null },
				{ Locale.getDefault(), null, null },
				{ Locale.getDefault(), Arrays.asList(), null },
				{ new Locale("de"), SUPPORTED, new Locale("de") },
				{ new Locale("de", "DE"), SUPPORTED, new Locale("de", "DE") },
				{ new Locale("fr"), SUPPORTED, null },
				{ new Locale("en"), SUPPORTED, new Locale("en") },
				{ new Locale("en", "US"), SUPPORTED, new Locale("en") }
		};
	}

	@DataProvider
	public Object[][] stringData()
	{
		return new Object[][] {
				{ null, null, null },
				{ null, Arrays.asList(), null },
				{ null, Arrays.asList("de-DE"), null },
				{ "de-DE", null, null },
				{ "de-DE", Arrays.asList(), null },
				{ "de", SUPPORTED, new Locale("de") },
				{ "de-DE", SUPPORTED, new Locale("de", "DE") },
				{ "fr", SUPPORTED, null },
				{ "en", SUPPORTED, new Locale("en") },
				{ "en-US", SUPPORTED, new Locale("en") }
		};
	}
}
