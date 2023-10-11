package io.github.furti.spring.web.extended.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.servlet.http.HttpServletRequest;

public final class RequestUtils
{

    private static final Pattern SESSIONIDPATTERN = Pattern.compile(";jsessionid=[^/]*");

    private RequestUtils()
    {

    }

    public static final String getPathFromRegex(HttpServletRequest request, Pattern pattern)
    {
        String path = request.getRequestURI();

        if (path == null || pattern == null)
        {
            return null;
        }

        //If the sessionId is encoded in the url strip it
        if (request.isRequestedSessionIdFromURL())
        {
            path = SESSIONIDPATTERN.matcher(path).replaceFirst("");
        }

        Matcher m = pattern.matcher(path);

        if (!m.matches() || m.groupCount() < 1)
        {
            return null;
        }

        return m.group(1);
    }
}
