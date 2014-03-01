package at.porscheinformatik.common.spring.web.extended.template;

import java.util.Locale;

import at.porscheinformatik.common.spring.web.extended.io.ResourceType;

public interface TemplateRenderContext
{
	Locale getLocale();

	ResourceType getResourceType();
}
