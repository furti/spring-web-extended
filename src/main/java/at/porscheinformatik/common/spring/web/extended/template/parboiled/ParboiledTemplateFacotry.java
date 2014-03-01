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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import at.porscheinformatik.common.spring.web.extended.expression.ExpressionHandlers;
import at.porscheinformatik.common.spring.web.extended.io.ResourceType;
import at.porscheinformatik.common.spring.web.extended.template.Template;
import at.porscheinformatik.common.spring.web.extended.template.TemplateFactory;

/**
 * @author Daniel Furtlehner
 * 
 */
public class ParboiledTemplateFacotry implements TemplateFactory
{

	private ExpressionHandlers expressionHandlers;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.porscheinformatik.common.spring.web.extended.template.TemplateFactory
	 * #createTemplate(org.springframework.core.io.Resource,
	 * at.porscheinformatik.common.spring.web.extended.io.ResourceType)
	 */
	@Override
	public Template createTemplate(Resource resource, String templateName,
			ResourceType type,
			boolean optimized)
	{
		try
		{
			return new ParboiledTemplate(resource, templateName,
					expressionHandlers, type,
					optimized);
		} catch (IOException ex)
		{
			throw new RuntimeException("Error creating template " + resource,
					ex);
		}
	}

	@Autowired
	public void setExpressionHandlers(ExpressionHandlers expressionHandlers)
	{
		this.expressionHandlers = expressionHandlers;
	}

}
