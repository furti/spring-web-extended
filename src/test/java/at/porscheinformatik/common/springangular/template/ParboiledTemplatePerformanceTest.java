package at.porscheinformatik.common.springangular.template;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import at.porscheinformatik.common.springangular.io.ResourceType;
import at.porscheinformatik.common.springangular.template.parboiled.ParboiledTemplate;

public class ParboiledTemplatePerformanceTest
{
	private Template template;
	private String expected;

	@BeforeTest
	public void setupParser() throws IOException
	{
		template = new ParboiledTemplate(
				new ClassPathResource(
						"/at/porscheinformatik/common/springangular/template/TestTemplate.html"),
				new TestExpressionHandlers(), ResourceType.HTML);

		InputStream expectedStream = new ClassPathResource(
				"/at/porscheinformatik/common/springangular/template/TestTemplateExpected.html")
				.getInputStream();

		expected = IOUtils.toString(expectedStream, Charset.forName("UTF-8"));
	}

	@Test
	public void testPerformance() throws IOException
	{
		for (int i = 0; i < 10000; i++)
		{
			assertThat(template.render(), equalTo(expected));
		}
	}

	@Test
	public void refreshTest() throws IOException
	{
		for (int i = 0; i < 1000; i++)
		{
			template.refresh();
		}
	}
}
