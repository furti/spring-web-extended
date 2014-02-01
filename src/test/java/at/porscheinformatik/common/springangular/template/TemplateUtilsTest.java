package at.porscheinformatik.common.springangular.template;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import at.porscheinformatik.common.springangular.template.cache.CacheUtils;

public class TemplateUtilsTest
{

	@Test(dataProvider = "buildPathData")
	public void buildPath(String configName, String path, String expected)
	{
		String actual = CacheUtils.buildPath(configName, path);

		assertThat(actual, equalTo(expected));
	}

	@DataProvider
	public Object[][] buildPathData()
	{
		return new Object[][] {
				{ null, null, "" },
				{ null, "test", "test" },
				{ null, "test.html", "test" },
				{ "", "test.html", "test" },
				{ "abc", "test.html", "abc/test" }
		};
	}
}
