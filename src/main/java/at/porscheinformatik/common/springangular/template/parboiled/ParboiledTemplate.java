package at.porscheinformatik.common.springangular.template.parboiled;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.parboiled.errors.ParseError;
import org.parboiled.parserunners.RecoveringParseRunner;
import org.parboiled.support.ParsingResult;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import at.porscheinformatik.common.springangular.resources.ResourceType;
import at.porscheinformatik.common.springangular.template.BaseTemplate;
import at.porscheinformatik.common.springangular.template.part.TemplatePart;

public class ParboiledTemplate extends BaseTemplate
{
	private Resource resource;
	private TemplateParser parser;
	private List<TemplatePart> parts;

	public ParboiledTemplate(Resource resource, TemplateParser parser,
			ResourceType type)
			throws IOException
	{
		super(type);
		Assert.notNull(resource, "Template resource must not be null");
		Assert.notNull(parser, "Template parser must not be null");
		this.resource = resource;
		this.parser = parser;
		refresh();
	}

	@Override
	protected String getTemplateUri()
	{
		return resource.getDescription();
	}

	@Override
	protected long getLastModified() throws IOException
	{
		return resource.lastModified();
	}

	@Override
	protected void doRefresh() throws IOException
	{
		Assert.isTrue(resource.isReadable(),
				"Template " + resource.getDescription()
						+ " is not readable");

		RecoveringParseRunner<TemplatePart> runner = new RecoveringParseRunner<TemplatePart>(
				parser.temlate());

		String templateData = null;
		try (InputStream in = resource.getInputStream())
		{
			// TODO: should we make the charset cofigurable?
			templateData = IOUtils.toString(resource.getInputStream(),
					Charset.forName("UTF-8"));
		}

		if (templateData == null)
		{
			parts = Collections.emptyList();
		}

		parts = buildParts(runner.run(templateData));
	}

	private List<TemplatePart> buildParts(ParsingResult<TemplatePart> result)
	{
		Assert.notNull(result, "Got null Result while parsing template "
				+ resource.getDescription());

		if (result.hasErrors())
		{
			throw new IllegalArgumentException(buildErrorMessag(result));
		}

		Assert.isTrue(result.matched,
				"Template " + resource.getDescription()
						+ " does not match the required format ");

		List<TemplatePart> parts = new ArrayList<>(result.valueStack.size());

		for (TemplatePart part : result.valueStack)
		{
			parts.add(0, part);
		}

		return parts;
	}

	private String buildErrorMessag(ParsingResult<TemplatePart> result)
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Template " + resource.getDescription()
				+ " contains errors: \n");

		for (ParseError error : result.parseErrors)
		{
			int start = error.getStartIndex();
			int end = error.getEndIndex();

			if (start - 10 > 0)
			{
				start -= 10;
			}
			else
			{
				start = 0;
			}

			builder.append(
					error.getInputBuffer().extract(start
							, end))
					.append(" --> ");

			if (error.getErrorMessage() != null)
			{
				builder.append(error.getErrorMessage());
			}
			else
			{
				builder.append(error.toString());
			}

			builder.append("\n");

			builder.append(StringUtils.leftPad("^",
					error.getStartIndex() - start - 1));

			if (error.getEndIndex() > error.getStartIndex())
			{
				builder.append(StringUtils.left("^", error.getEndIndex()
						- error.getStartIndex() - 1));
			}
		}

		return builder.toString();
	}

	@Override
	protected String getContent() throws IOException
	{
		if (CollectionUtils.isEmpty(parts))
		{
			return "";
		}

		StringBuilder builder = new StringBuilder();

		for (TemplatePart part : parts)
		{
			builder.append(part.render());
		}

		return builder.toString();
	}
}
