package at.porscheinformatik.common.springangular.template;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.parboiled.Parboiled;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import at.porscheinformatik.common.springangular.expression.ExpressionHandler;
import at.porscheinformatik.common.springangular.expression.ExpressionHandlers;
import at.porscheinformatik.common.springangular.expression.TemplateExpressionHandlers;
import at.porscheinformatik.common.springangular.resources.ClasspathResourceScanner;
import at.porscheinformatik.common.springangular.resources.ResourceScanner;
import at.porscheinformatik.common.springangular.resources.ResourceScanners;
import at.porscheinformatik.common.springangular.template.cache.DefaultTemplateEntryConfig;
import at.porscheinformatik.common.springangular.template.cache.TemplateEntryConfig;
import at.porscheinformatik.common.springangular.template.cache.html.HtmlTemplateCache;
import at.porscheinformatik.common.springangular.template.parboiled.TemplateParser;

public class TemplateCacheTest
{

	@Test
	public void hasTemplate() throws IOException
	{
		HtmlTemplateCache cache = new HtmlTemplateCache(getConfig());
		// cache.setHandlers(createExpressionHandlers());
		cache.setParser(Parboiled.createParser(TemplateParser.class,
				createExpressionHandlers()));
		cache.setScanners(new ResourceScanners(getScanners()));
		cache.setLocaleContex(new SimpleLocaleContext(Locale.getDefault()));
		cache.setupTemplates();

		assertThat(cache.hasTemplate("index"), equalTo(true));
		assertThat(cache.hasTemplate("other/test"), equalTo(true));
		assertThat(cache.hasTemplate("other/test2"), equalTo(true));
	}

	@Test(dataProvider = "renderData")
	public void renderTemplate(String template, Locale locale, String expected)
			throws IOException
	{
		HtmlTemplateCache cache = new HtmlTemplateCache(getConfig());
		// cache.setHandlers(createExpressionHandlers());
		cache.setParser(Parboiled.createParser(TemplateParser.class,
				createExpressionHandlers()));
		cache.setScanners(new ResourceScanners(getScanners()));
		cache.setLocaleContex(new SimpleLocaleContext(locale));
		cache.setupTemplates();

		assertThat(cache.renderTemplate(template), equalTo(expected));
	}

	@DataProvider
	public Object[][] renderData()
	{
		return new Object[][] {
				{ "index", Locale.GERMAN, "index abc" },
				{ "index", Locale.FRANCE, "index abc" },
				{ "index", Locale.ENGLISH, "this is the index page" },
				{ "other/test", Locale.getDefault(), "test" },
				{ "other/test2", Locale.getDefault(),
						"abc othertest noch mal\r\ntest bcd" }
		};
	}

	private ExpressionHandlers createExpressionHandlers()
	{
		Map<String, ExpressionHandler> map = new HashMap<>();
		map.put("message", new ExpressionHandler() {

			@Override
			public String process(String value)
			{
				return value;
			}
		});

		TemplateExpressionHandlers handlers = new TemplateExpressionHandlers(
				map);
		return handlers;
	}

	private Map<String, TemplateEntryConfig> getConfig()
	{
		Map<String, TemplateEntryConfig> config = new HashMap<String, TemplateEntryConfig>();
		config.put(
				"",
				new DefaultTemplateEntryConfig(
						"classpath:at/porscheinformatik/common/springangular/testtemplates"));
		return config;
	}

	private Map<String, ResourceScanner> getScanners()
	{

		Map<String, ResourceScanner> scanners = new HashMap<>();
		scanners.put("classpath", new ClasspathResourceScanner());

		return scanners;
	}
}
