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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class DefaultApplicationConfiguration implements
		ApplicationConfiguration
{
	private String version;
	private boolean optimizeResources;
	private List<Locale> supportedLocales = new ArrayList<>();

	@Override
	public String getVersion()
	{
		return version;
	}

	@Override
	public boolean isOptimizeResources()
	{
		return optimizeResources;
	}

	@Override
	public ApplicationConfiguration setOptimizeResources(
			boolean optimizeResources)
	{
		this.optimizeResources = optimizeResources;

		return this;
	}

	@Override
	public ApplicationConfiguration setVersion(String version)
	{
		this.version = version;

		return this;
	}

	@Override
	public ApplicationConfiguration addLocale(String language)
	{

		return addLocale(language, "", "");
	}

	@Override
	public ApplicationConfiguration addLocale(String language, String country)
	{
		return addLocale(language, country, "");
	}

	@Override
	public ApplicationConfiguration addLocale(String language, String country,
			String variant)
	{
		Locale locale = new Locale(language, country, variant);

		supportedLocales.add(locale);

		return this;
	}

	@Override
	public ApplicationConfiguration removeLocale(String language)
	{
		return removeLocale(language, null, null);
	}

	@Override
	public ApplicationConfiguration removeLocale(String language, String country)
	{
		return removeLocale(language, country, null);
	}

	@Override
	public ApplicationConfiguration removeLocale(String language,
			String country, String variant)
	{
		Locale locale = new Locale(language, country, variant);

		supportedLocales.remove(locale);

		return this;
	}

	@Override
	public List<Locale> getSupportedLocales()
	{
		return Collections.unmodifiableList(supportedLocales);
	}
}
