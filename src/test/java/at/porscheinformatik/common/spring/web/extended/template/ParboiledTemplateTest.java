package at.porscheinformatik.common.spring.web.extended.template;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import at.porscheinformatik.common.spring.web.extended.io.ResourceType;
import at.porscheinformatik.common.spring.web.extended.template.parboiled.ParboiledTemplate;

public class ParboiledTemplateTest
{

	@Test(dataProvider = "templateData")
	public void template(Resource resource, String expected)
			throws IOException
	{
		String actual = new ParboiledTemplate(resource,
				resource.getDescription(),
				new TestExpressionHandlers(),
				ResourceType.HTML, false).render();

		assertThat(actual, equalTo(expected));
	}

	@DataProvider
	public Object[][] templateData()
	{
		return new Object[][] {
				{ new StringResource("Template without expressions"),
						"Template without expressions" },
				{ new StringResource("Template $ without { expressions }"),
						"Template $ without { expressions }" },
				{ new StringResource(
						"Template with single expression ${simple:value}"),
						"Template with single expression value" },
				{ new StringResource(
						"Template with ${static}"),
						"Template with static text" },
				{ new StringResource(
						"${simple:value} Template with starting expression"),
						"value Template with starting expression" },
				{ new StringResource(
						"${simple:onlyexpression}"),
						"onlyexpression" },
				{ new StringResource(
						"${simple:firstexpression}${simple:secondexpression}"),
						"firstexpressionsecondexpression" },
				{
						new StringResource(
								"${simple:firstexpression} ${simple:secondexpression}"),
						"firstexpression secondexpression" },
				{
						new StringResource(
								"Template with multiple expressions. ${simple:first} and ${simple:second}"),
						"Template with multiple expressions. first and second" },
				{
						new StringResource(
								"Template with multiple expressions. ${simple:first} and ${simple:second} and some static text at the end"),
						"Template with multiple expressions. first and second and some static text at the end" }
		};
	}

	@Test(dataProvider = "customExpressionData")
	public void customExpression(Resource resource, String expected)
			throws IOException
	{
		String actual = new ParboiledTemplate(resource,
				resource.getDescription(),
				new TestExpressionHandlers(), "{{", "|", "}}",
				ResourceType.HTML, false).render();

		assertThat(actual, equalTo(expected));
	}

	@DataProvider
	public Object[][] customExpressionData()
	{
		return new Object[][] {
				{ new StringResource("Template without expressions"),
						"Template without expressions" },
				{ new StringResource("Template $ without { expressions }"),
						"Template $ without { expressions }" },
				{ new StringResource(
						"Template with single expression {{simple|value}}"),
						"Template with single expression value" },
				{ new StringResource(
						"{{simple|value}} Template with starting expression"),
						"value Template with starting expression" },
				{ new StringResource(
						"{{simple|onlyexpression}}"),
						"onlyexpression" },
				{
						new StringResource(
								"{{simple|firstexpression}}{{simple|secondexpression}}"),
						"firstexpressionsecondexpression" },
				{
						new StringResource(
								"{{simple|firstexpression}} {{simple|secondexpression}}"),
						"firstexpression secondexpression" },
				{
						new StringResource(
								"Template with multiple expressions. {{simple|first}} and {{simple|second}}"),
						"Template with multiple expressions. first and second" },
				{
						new StringResource(
								"Template with multiple expressions. {{simple|first}} and {{simple|second}} and some static text at the end"),
						"Template with multiple expressions. first and second and some static text at the end" }
		};
	}
}