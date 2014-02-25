package at.porscheinformatik.common.springangular.expression.parser;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.parboiled.Parboiled;
import org.parboiled.parserunners.RecoveringParseRunner;
import org.parboiled.support.ParsingResult;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import at.porscheinformatik.common.spring.web.extended.expression.parser.InlineTemplateParser;

public class InlineTemplateParserTest
{

	@Test(dataProvider = "inlineTemplateData")
	public void inlineTemplate(String input, List<String> expected)
	{
		InlineTemplateParser parser = Parboiled
				.createParser(InlineTemplateParser.class);

		RecoveringParseRunner<String> runner = new RecoveringParseRunner<String>(
				parser.inlineTemplate());

		ParsingResult<String> result = runner.run(input);

		List<String> actual = new ArrayList<String>();

		for (String s : result.valueStack)
		{
			actual.add(0, s);
		}

		assertThat(actual, equalTo(expected));
	}

	@DataProvider
	public Object[][] inlineTemplateData()
	{
		return new Object[][] {
				{ "<p>DasisteinTest</p>",
						Arrays.asList("<p>DasisteinTest</p>") },
				{ "<p>Das ist ein Test</p>",
						Arrays.asList("<p>Das ist ein Test</p>") },
				{
						"<p>Das ist ein Test</p>\n<p>mit mehreren Zeilen</p>",
						Arrays.asList("<p>Das ist ein Test</p>",
								" ",
								"<p>mit mehreren Zeilen</p>") },
				{
						"<p>Das ist ein Test</p>\n	<p>mit mehreren Zeilen</p>\n\r	<p>und leerzeichen</p>",
						Arrays.asList("<p>Das ist ein Test</p>",
								" ",
								"<p>mit mehreren Zeilen</p>",
								"",
								" ",
								"<p>und leerzeichen</p>") }
		};
	}
}
