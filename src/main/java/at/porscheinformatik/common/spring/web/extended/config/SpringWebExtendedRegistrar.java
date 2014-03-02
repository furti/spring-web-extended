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
package at.porscheinformatik.common.spring.web.extended.config;

import java.util.Map;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

import at.porscheinformatik.common.spring.web.extended.annotation.EnableSpringWebExtended;
import at.porscheinformatik.common.spring.web.extended.asset.AssetController;
import at.porscheinformatik.common.spring.web.extended.template.cache.html.HtmlTemplateController;
import at.porscheinformatik.common.spring.web.extended.template.cache.script.ScriptController;
import at.porscheinformatik.common.spring.web.extended.template.cache.style.StyleController;

public class SpringWebExtendedRegistrar implements
		ImportBeanDefinitionRegistrar
{

	private static final String TEMPLATECONTROLLER_KEY = "htmlTemplateControllerConfig";
	private static final String TEMPLATECONTROLLERREGISTER_KEY = "registerHtmlTemplateController";
	private static final String TEMPLATECONTROLLERFALLBACK_KEY = "fallbackToIndex";
	private static final String ASSETCONTROLLER_KEY = "registerAssetController";
	private static final String STYLECONTROLLER_KEY = "registerStyleController";
	private static final String SCRIPTCONTROLLER_KEY = "registerScriptController";

	@Override
	public void registerBeanDefinitions(
			AnnotationMetadata annotationMetadata,
			BeanDefinitionRegistry registry)
	{

		Map<String, Object> annotationAttributes = annotationMetadata
				.getAnnotationAttributes(EnableSpringWebExtended.class
						.getName());

		// Guard against calls for sub-classes
		if (annotationAttributes == null)
		{
			return;
		}

		handleTemplateController(annotationAttributes, registry);
		handleAssetController(annotationAttributes, registry);
		handleStyleController(annotationAttributes, registry);
		handleScriptController(annotationAttributes, registry);
	}

	@SuppressWarnings("unchecked")
	private void handleTemplateController(
			Map<String, Object> annotationAttributes,
			BeanDefinitionRegistry registry)
	{
		Map<String, Object> templateControllerConfig = (Map<String, Object>) annotationAttributes
				.get(TEMPLATECONTROLLER_KEY);

		Assert.notNull(templateControllerConfig,
				"No config for TemplateController found in EnableSpringWebExtended");

		Boolean register = (Boolean) templateControllerConfig
				.get(TEMPLATECONTROLLERREGISTER_KEY);
		Boolean fallback = (Boolean) templateControllerConfig
				.get(TEMPLATECONTROLLERFALLBACK_KEY);

		if (register != null && register.booleanValue())
		{
			registerTemplateController(registry, fallback);
		}
	}

	private void handleAssetController(
			Map<String, Object> annotationAttributes,
			BeanDefinitionRegistry registry)
	{
		Boolean register = (Boolean) annotationAttributes
				.get(ASSETCONTROLLER_KEY);

		if (register != null && register.booleanValue())
		{
			registerAssetController(registry);
		}
	}

	private void handleStyleController(
			Map<String, Object> annotationAttributes,
			BeanDefinitionRegistry registry)
	{
		Boolean register = (Boolean) annotationAttributes
				.get(STYLECONTROLLER_KEY);

		if (register != null && register.booleanValue())
		{
			registerStyleController(registry);
		}
	}

	private void handleScriptController(
			Map<String, Object> annotationAttributes,
			BeanDefinitionRegistry registry)
	{
		Boolean register = (Boolean) annotationAttributes
				.get(SCRIPTCONTROLLER_KEY);

		if (register != null && register.booleanValue())
		{
			registerScriptController(registry);
		}
	}

	private void registerAssetController(BeanDefinitionRegistry registry)
	{
		BeanDefinitionBuilder builder = BeanDefinitionBuilder
				.rootBeanDefinition(AssetController.class);

		registry.registerBeanDefinition("assetController",
				builder.getBeanDefinition());
	}

	private void registerStyleController(BeanDefinitionRegistry registry)
	{
		BeanDefinitionBuilder builder = BeanDefinitionBuilder
				.rootBeanDefinition(StyleController.class);

		registry.registerBeanDefinition("styleController",
				builder.getBeanDefinition());
	}

	private void registerScriptController(BeanDefinitionRegistry registry)
	{
		BeanDefinitionBuilder builder = BeanDefinitionBuilder
				.rootBeanDefinition(ScriptController.class);

		registry.registerBeanDefinition("scriptController",
				builder.getBeanDefinition());
	}

	private void registerTemplateController(BeanDefinitionRegistry registry,
			Boolean fallback)
	{
		BeanDefinitionBuilder builder = BeanDefinitionBuilder
				.rootBeanDefinition(HtmlTemplateController.class);
		builder.addConstructorArgValue(fallback);

		registry.registerBeanDefinition("templateController",
				builder.getBeanDefinition());
	}
}
