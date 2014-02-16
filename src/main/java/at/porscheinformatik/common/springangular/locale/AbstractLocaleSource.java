package at.porscheinformatik.common.springangular.locale;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;

public abstract class AbstractLocaleSource implements LocaleSource
{
	@Override
	public Locale getLocale(HttpServletRequest request,
			HttpServletResponse response)
	{
		String possibleLocale = getPossibleLocale(request, response);

		return checkLocale(possibleLocale);
	}

	private Locale checkLocale(String possibleLocale)
	{
		if (StringUtils.isEmpty(possibleLocale))
		{
			return null;
		}
		
		for (Locale locale : Locale.getAvailableLocales())
		{
			if (locale.toString().equals(possibleLocale))
			{
				return locale;
			}
		}

		return null;
	}

	protected abstract String getPossibleLocale(HttpServletRequest request,
			HttpServletResponse response);
}
