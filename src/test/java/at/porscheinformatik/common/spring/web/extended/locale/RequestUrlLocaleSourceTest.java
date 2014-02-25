package at.porscheinformatik.common.spring.web.extended.locale;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.mockito.Mockito;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import at.porscheinformatik.common.spring.web.extended.locale.PathInfoLocaleSource;

public class RequestUrlLocaleSourceTest
{

	@Test(dataProvider = "getLocaleData")
	public void getLocale(String pathInfo, Locale expected)
	{
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getPathInfo()).thenReturn(pathInfo);
		PathInfoLocaleSource source = new PathInfoLocaleSource();

		Locale actual = source.getLocale(request, null);

		assertThat(actual, equalTo(expected));
	}

	@DataProvider
	public Object[][] getLocaleData()
	{
		return new Object[][] {
				{ null, null },
				{ "", null },
				{ "/", null },
				{ "/test", null },
				{ "/abc/test", null },
				{ "/de/test", new Locale("de") },
				{ "/de_AT/test", new Locale("de", "AT") },
				{ "/xy_AB/test", null },
		};
	}
}
