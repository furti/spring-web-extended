package at.porscheinformatik.common.springangular.template.parboiled;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.parboiled.parserunners.RecoveringParseRunner;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import at.porscheinformatik.common.springangular.io.ResourceType;
import at.porscheinformatik.common.springangular.template.BaseTemplate;
import at.porscheinformatik.common.springangular.template.part.TemplatePart;
import at.porscheinformatik.common.springangular.util.ParboiledUtils;

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

		parts = ParboiledUtils.buildFromResult(runner.run(templateData),
				resource.getDescription());
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
