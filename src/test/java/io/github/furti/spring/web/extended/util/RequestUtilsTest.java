package io.github.furti.spring.web.extended.util;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class RequestUtilsTest
{

    @Test
    public void getPathFromRegex()
    {
        performgetPathFromRegex(null, "", null, false);
        performgetPathFromRegex("/test/templates/ab/cd", "^.*templates/(.*)", "ab/cd", false);
        performgetPathFromRegex("/test/templates/ab/cd", "^.*template/(.*)", null, false);
        performgetPathFromRegex("/test/templates/ab/cd;jsessionid=Aabasce", "^.*templates/(.*)", "ab/cd", true);
        performgetPathFromRegex("/test/templates/ab;jsessionid=abasce/cd", "^.*templates/(.*)", "ab/cd", true);
    }

    private void performgetPathFromRegex(String uri, String regex, String expected, boolean sessionidFromURL)
    {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getRequestURI()).thenReturn(uri);
        Mockito.when(request.isRequestedSessionIdFromURL()).thenReturn(sessionidFromURL);

        Pattern pattern = Pattern.compile(regex);

        String actual = RequestUtils.getPathFromRegex(request, pattern);

        assertThat(actual, equalTo(expected));
    }
}
