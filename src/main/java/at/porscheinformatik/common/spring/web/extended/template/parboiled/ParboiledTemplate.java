/**
 * Copyright 2014 Daniel Furtlehner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.porscheinformatik.common.spring.web.extended.template.parboiled;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.parboiled.Parboiled;
import org.parboiled.parserunners.RecoveringParseRunner;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import at.porscheinformatik.common.spring.web.extended.expression.ExpressionHandlers;
import at.porscheinformatik.common.spring.web.extended.io.ResourceType;
import at.porscheinformatik.common.spring.web.extended.template.BaseTemplate;
import at.porscheinformatik.common.spring.web.extended.template.part.TemplatePart;
import at.porscheinformatik.common.spring.web.extended.util.ParboiledUtils;

public class ParboiledTemplate extends BaseTemplate
{
	private Resource resource;
	private List<TemplatePart> parts;
	private ExpressionHandlers expressionHandlers;
	private String expressionPrefix, expressionSuffix, expressionDelimiter;

	public ParboiledTemplate(Resource resource,
			String templateName,
			ExpressionHandlers expressionHandlers,
			ResourceType type,
			boolean alreadyOptimized)
			throws IOException
	{
		this(resource, templateName, expressionHandlers, null, null, null,
				type,
				alreadyOptimized);
	}

	public ParboiledTemplate(Resource resource,
			String templateName,
			ExpressionHandlers expressionHandlers,
			String expressionPrefix,
			String expressionDelimiter,
			String expressionSuffix,
			ResourceType type,
			boolean alreadyOptimized)
			throws IOException
	{
		super(type, templateName, alreadyOptimized);
		Assert.notNull(resource, "Template resource must not be null");
		Assert.notNull(expressionHandlers,
				"Expressionhandlers must not be null");
		this.resource = resource;
		this.expressionHandlers = expressionHandlers;
		this.expressionDelimiter = expressionDelimiter;
		this.expressionPrefix = expressionPrefix;
		this.expressionSuffix = expressionSuffix;
		this.resource = resource;
		refresh();
	}

	@Override
	protected void doRefresh() throws IOException
	{
		Assert.isTrue(resource.isReadable(),
				"Template " + resource.getDescription()
						+ " is not readable");

		TemplateParser parser = buildParser();

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

	private TemplateParser buildParser()
	{
		if (expressionDelimiter != null && expressionSuffix != null
				&& expressionPrefix != null)
		{
			return Parboiled.createParser(TemplateParser.class,
					expressionHandlers, expressionPrefix, expressionSuffix,
					expressionDelimiter);
		}
		else
		{
			return Parboiled.createParser(TemplateParser.class,
					expressionHandlers);
		}
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

	@Override
	protected long getLastModified() throws IOException
	{
		return resource.lastModified();
	}
}
