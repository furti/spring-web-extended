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
package at.porscheinformatik.common.spring.web.extended.template;

import java.util.Locale;

import at.porscheinformatik.common.spring.web.extended.io.ResourceType;

public class DefaultTemplateRenderContext implements TemplateRenderContext
{
	private Locale locale;
	private ResourceType resourceType;

	public DefaultTemplateRenderContext(Locale locale, ResourceType resourceType)
	{
		super();
		this.locale = locale;
		this.resourceType = resourceType;
	}

	public Locale getLocale()
	{
		return locale;
	}

	public void setLocale(Locale locale)
	{
		this.locale = locale;
	}

	public ResourceType getResourceType()
	{
		return resourceType;
	}

	public void setResourceType(ResourceType resourceType)
	{
		this.resourceType = resourceType;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((locale == null) ? 0 : locale.hashCode());
		result = prime * result
				+ ((resourceType == null) ? 0 : resourceType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}

		DefaultTemplateRenderContext other = (DefaultTemplateRenderContext) obj;

		if (locale == null)
		{
			if (other.locale != null)
			{
				return false;
			}
		} else if (!locale.equals(other.locale))
		{
			return false;
		}

		if (resourceType != other.resourceType)
		{
			return false;
		}

		return true;
	}

}
