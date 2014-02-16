package at.porscheinformatik.common.springangular.locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;

public class PathInfoLocaleSource extends AbstractLocaleSource
{

	@Override
	protected String getPossibleLocale(HttpServletRequest request,
			HttpServletResponse response)
	{
		String path = request.getPathInfo();

		if (StringUtils.isEmpty(path))
		{
			return null;
		}

		// The first character is a / so we start searching for the next / at
		// index 1
		int possibleLocaleEnd = path.indexOf('/', 1);

		// If not / is found maybe it is only a locale
		if (possibleLocaleEnd == -1)
		{
			possibleLocaleEnd = path.length() - 1;
		}

		if (possibleLocaleEnd < 2)
		{
			return null;
		}

		return path.substring(1, possibleLocaleEnd);
	}
}
