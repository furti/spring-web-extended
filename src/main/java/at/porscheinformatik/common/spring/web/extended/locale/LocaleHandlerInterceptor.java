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
package at.porscheinformatik.common.spring.web.extended.locale;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.Assert;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author Daniel Furtlehner
 * 
 */
public class LocaleHandlerInterceptor extends HandlerInterceptorAdapter
{

	private List<LocaleSource> sources;
	private List<Locale> availableLocales;

	public LocaleHandlerInterceptor(List<LocaleSource> sources)
	{
		super();
		Assert.notEmpty(sources, "LocaleSources must not be empty");
		this.sources = sources;
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception
	{
		for (LocaleSource source : sources)
		{
			Locale locale = source.getLocale(request, response);

			if (locale != null)
			{
				LocaleContextHolder.setLocale(locale, true);
				return true;
			}
		}

		// If no Locale is available we simply use the one from the request
		if (availableLocales == null || availableLocales.size() == 0)
		{
			return true;
		}

		// If no locale was found we check if the one from the request is
		// supported
		Locale locale = LocaleContextHolder.getLocale();

		if (availableLocales.contains(locale))
		{
			return true;
		}

		// If the locale from the request is not supported we use the first
		// supported locale
		LocaleContextHolder.setLocale(availableLocales.get(0));

		return true;
	}

	@Autowired
	public void setAvailableLocales(List<Locale> availableLocales)
	{
		this.availableLocales = availableLocales;
	}
}
