package at.porscheinformatik.common.springangular.resources;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import at.porscheinformatik.common.springangular.resources.ResourceUtils;

public class ResourceUtilsTest
{

	@Test(dataProvider = "localizedResourcesData")
	public void buildLocalizedTemplateNames(String name, Locale locale,
			List<String> expected)
	{
		List<String> actual = ResourceUtils.localizedResources(name, locale);

		assertThat(actual, equalTo(expected));
	}

	@Test(dataProvider = "getNameAndEndingData")
	public void getNameAndEnding(String file, String[] expected)
	{
		String[] actual = ResourceUtils.getNameAndEnding(file);

		assertThat(actual, equalTo(expected));
	}

	@Test(dataProvider = "pathAndFileData")
	public void pathAndFile(String location, String[] expected)
	{
		String[] actual = ResourceUtils.pathAndFile(location);

		assertThat(actual, equalTo(expected));
	}

	@DataProvider
	public Object[][] localizedResourcesData()
	{
		return new Object[][] {
				{ null, null, null },
				{ null, Locale.GERMAN, null },
				{ "index", null, Arrays.asList("index") },
				{ "index", new Locale("de"), Arrays.asList("index_de", "index") },
				{ "index.html", new Locale("de"),
						Arrays.asList("index_de.html", "index.html") },
				{ "index", new Locale("de", "AT"),
						Arrays.asList("index_de_at", "index_de", "index") },
				{ "index", new Locale("de", "AT", "ABC"),
						Arrays.asList("index_de_at_abc", "index_de_at",
								"index_de", "index") }
		};
	}

	@DataProvider
	public Object[][] pathAndFileData()
	{
		return new Object[][] {
				{ null, null },
				{ "", new String[] { "", "" } },
				{ "test", new String[] { "", "test" } },
				{ "/test", new String[] { "/", "test" } },
				{ "/abc/de/test", new String[] { "/abc/de/", "test" } }
		};
	}

	@DataProvider
	public Object[][] getNameAndEndingData()
	{
		return new Object[][] {
				{ null, null },
				{ "test.jpg", new String[] { "test", ".jpg" } }
		};
	}
}
