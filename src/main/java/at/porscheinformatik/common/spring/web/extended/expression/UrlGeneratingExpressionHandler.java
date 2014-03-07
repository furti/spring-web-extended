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
package at.porscheinformatik.common.spring.web.extended.expression;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

import at.porscheinformatik.common.spring.web.extended.config.ApplicationConfiguration;

/**
 * @author Daniel Furtlehner
 * 
 */
public abstract class UrlGeneratingExpressionHandler implements
		ExpressionHandler
{
	private ApplicationConfiguration appConfig;

	protected String generateUrl(String... parts)
	{
		if (parts == null)
		{
			return null;
		}

		StringBuilder url = new StringBuilder();

		url.append(LocaleContextHolder.getLocale().toLanguageTag());

		if (appConfig != null && appConfig.getVersion() != null)
		{
			url.append("/").append(appConfig.getVersion());
		}

		for (String part : parts)
		{
			url.append("/").append(part);
		}

		return url.toString();
	}

	@Autowired
	public void setAppConfig(ApplicationConfiguration appConfig)
	{
		this.appConfig = appConfig;
	}
}
