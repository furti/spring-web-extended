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
package at.porscheinformatik.common.spring.web.extended.template.chunk;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;

import jline.internal.InputStreamReader;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;

import at.porscheinformatik.common.spring.web.extended.io.ResourceType;
import at.porscheinformatik.common.spring.web.extended.template.BaseTemplate;

import com.x5.template.Chunk;
import com.x5.template.Theme;

public class ChunkTemplate extends BaseTemplate
{
	private Resource resource;
	private Theme theme;
	private String templateContent;

	public ChunkTemplate(ResourceType type, String templateName,
			boolean alreadyOptimized, Resource resource, Theme theme)
	{
		super(type, templateName, alreadyOptimized);
		this.resource = resource;
		this.theme = theme;
	}

	@Override
	protected String getContent() throws IOException
	{
		Chunk chunk = buildChunk();
		return chunk.toString();
	}

	@Override
	protected void doRefresh() throws IOException
	{
		try (Reader reader = new InputStreamReader(resource.getInputStream(),
				Charset.forName("UTF-8")))
		{
			templateContent = IOUtils.toString(reader);
		}
	}

	@Override
	protected long getLastModified() throws IOException
	{
		return resource.lastModified();
	}

	private Chunk buildChunk()
	{
		Chunk chunk = theme.makeChunk();
		chunk.append(templateContent);

		return chunk;
	}
}
