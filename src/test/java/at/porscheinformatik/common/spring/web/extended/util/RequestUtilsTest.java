package at.porscheinformatik.common.spring.web.extended.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.mockito.Mockito;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RequestUtilsTest
{

    @Test(dataProvider = "regexData")
    public void getPathFromRegex(String uri, String regex, String expected, boolean sessionidFromURL)
    {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getRequestURI()).thenReturn(uri);
        Mockito.when(request.isRequestedSessionIdFromURL()).thenReturn(sessionidFromURL);

        Pattern pattern = Pattern.compile(regex);

        String actual = RequestUtils.getPathFromRegex(request, pattern);

        assertThat(actual, equalTo(expected));
    }

    @DataProvider
    public Object[][] regexData()
    {
        return new Object[][]{
            {null, "", null, false},
            {"/test/templates/ab/cd", "^.*templates/(.*)", "ab/cd", false},
            {"/test/templates/ab/cd", "^.*template/(.*)", null, false},
            {"/test/templates/ab/cd;jsessionid=Aabasce", "^.*templates/(.*)", "ab/cd", true},
            {"/test/templates/ab;jsessionid=abasce/cd", "^.*templates/(.*)", "ab/cd", true}
        };
    }
}
