package io.github.furti.spring.web.extended.util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.github.furti.spring.web.extended.util.HtmlUtils;

public class HtmlUtilsTest
{

    @Test(dataProvider = "scriptData")
    public void buildScriptLink(String url, String expected)
    {
        String actual = HtmlUtils.buildScriptLink(url);

        assertThat(actual, equalTo(expected));
    }

    @Test(dataProvider = "styleData")
    public void buildStyleLink(String url, String expected)
    {
        String actual = HtmlUtils.buildStyleLink(url);

        assertThat(actual, equalTo(expected));
    }

    @DataProvider
    public Object[][] scriptData()
    {
        return new Object[][]{
            {null, "<script src=\"\" type=\"text/javascript\"></script>"},
            {"", "<script src=\"\" type=\"text/javascript\"></script>"},
            {"test", "<script src=\"test\" type=\"text/javascript\"></script>"},
            {"test/test.js", "<script src=\"test/test.js\" type=\"text/javascript\"></script>"}};
    }

    @DataProvider
    public Object[][] styleData()
    {
        return new Object[][]{
            {null, "<link href=\"\" type=\"text/css\" rel=\"stylesheet\">"},
            {"", "<link href=\"\" type=\"text/css\" rel=\"stylesheet\">"},
            {"test", "<link href=\"test\" type=\"text/css\" rel=\"stylesheet\">"},
            {"test/test.css", "<link href=\"test/test.css\" type=\"text/css\" rel=\"stylesheet\">"}};
    }
}
