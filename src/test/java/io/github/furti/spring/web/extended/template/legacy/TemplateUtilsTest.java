package io.github.furti.spring.web.extended.template.legacy;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.github.furti.spring.web.extended.template.legacy.cache.CacheUtils;

public class TemplateUtilsTest
{

    @Test(dataProvider = "buildPathData")
    public void buildPath(String configName, String path, String expected)
    {
        String actual = CacheUtils.buildPath(configName, path);

        assertThat(actual, equalTo(expected));
    }

    @DataProvider
    public Object[][] buildPathData()
    {
        return new Object[][]{
            {null, null, ""},
            {null, "test", "test"},
            {null, "test.html", "test"},
            {"", "test.html", "test"},
            {"abc", "test.html", "abc/test"}};
    }
}
