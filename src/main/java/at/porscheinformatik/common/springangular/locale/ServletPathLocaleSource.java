package at.porscheinformatik.common.springangular.locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;

/**
 * LocaleSource that uses the servletPath as a possible Locale
 * 
 * @author Daniel Furtlehner
 * 
 */
public class ServletPathLocaleSource extends AbstractLocaleSource
{

	@Override
	protected String getPossibleLocale(HttpServletRequest request,
			HttpServletResponse response)
	{
		String servletPath = request.getServletPath();

		if (StringUtils.hasText(servletPath))
		{
			// Cut the leading /
			return servletPath.substring(1);
		}

		return null;
	}
}
