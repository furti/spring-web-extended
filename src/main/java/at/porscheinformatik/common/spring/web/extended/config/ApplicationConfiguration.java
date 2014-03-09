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

import java.util.List;
import java.util.Locale;

public interface ApplicationConfiguration
{

	String getVersion();

	ApplicationConfiguration setVersion(String version);

	boolean isOptimizeResources();

	ApplicationConfiguration setOptimizeResources(boolean optimizeResources);

	ApplicationConfiguration addLocale(String language);

	ApplicationConfiguration addLocale(String language, String country);

	ApplicationConfiguration addLocale(String language, String country,
			String variant);

	ApplicationConfiguration removeLocale(String language);

	ApplicationConfiguration removeLocale(String language, String country);

	ApplicationConfiguration removeLocale(String language, String country,
			String variant);

	List<Locale> getSupportedLocales();
}
