package at.porscheinformatik.common.springangular.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public final class RequestUtils
{

	private RequestUtils()
	{

	}

	public static final String getPathFromRegex(HttpServletRequest request,
			Pattern pattern)
	{
		String path = request.getServletPath() != null
				? request.getServletPath()
				: "";

		if (request.getPathInfo() != null)
		{
			path += request.getPathInfo();
		}

		Matcher m = pattern.matcher(path);

		if (!m.matches() || m.groupCount() < 1)
		{
			return null;
		}

		return m.group(1);
	}
}
