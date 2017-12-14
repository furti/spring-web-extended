package io.github.furti.spring.web.extended.io;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.github.furti.spring.web.extended.io.ClasspathResourceScanner;

public class ClasspathResourceScannerTest
{

    @Test
    public void scanResources() throws IOException
    {
        ClasspathResourceScanner scanner = new ClasspathResourceScanner();

        Map<String, Resource> actual =
            scanner.scanResources("io/github/furti/spring/web/extended/testtemplates/**/*", null);
        assertThat(actual, notNullValue());
        assertThat(actual.size(), equalTo(5));
        assertThat(actual.containsKey("index.txt"), equalTo(true));
        assertThat(actual.containsKey("index_en.txt"), equalTo(true));
        assertThat(actual.containsKey("index.min.txt"), equalTo(true));
        assertThat(actual.containsKey("other/test.txt"), equalTo(true));
        assertThat(actual.containsKey("other/test2.txt"), equalTo(true));
    }

    @Test
    public void scanResourcesWithFileAndNoSubdirectories() throws IOException
    {
        ClasspathResourceScanner scanner = new ClasspathResourceScanner();

        Map<String, Resource> actual =
            scanner.scanResources("io/github/furti/spring/web/extended/testtemplates", "index.txt", false);

        assertThat(actual, notNullValue());
        assertThat(actual.size(), equalTo(2));
        assertThat(actual.containsKey("index.txt"), equalTo(true));
        assertThat(actual.containsKey("index_en.txt"), equalTo(true));
    }

    @Test(dataProvider = "buildScanPathData")
    public void buildScanPath(String path, String file, boolean scanSubDirectories, String expected)
    {
        ClasspathResourceScanner scanner = new ClasspathResourceScanner();

        String actual = scanner.buildScanPath(path, file, scanSubDirectories);

        assertThat(actual, equalTo(expected));
    }

    @DataProvider
    public Object[][] buildScanPathData()
    {
        return new Object[][]{
            {
                "/io/github/furti/springangular/testtemplates",
                "test.html",
                false,
                "classpath*:/io/github/furti/springangular/testtemplates/test*.html"},
            {
                "/io/github/furti/springangular/testtemplates",
                null,
                false,
                "classpath*:/io/github/furti/springangular/testtemplates/*.*"},
            {
                "/io/github/furti/springangular/testtemplates",
                "test",
                false,
                "classpath*:/io/github/furti/springangular/testtemplates/test*.*"},
            {
                "/io/github/furti/springangular/testtemplates",
                "test.html",
                true,
                "classpath*:/io/github/furti/springangular/testtemplates/**/test*.html"},
            {
                "/io/github/furti/springangular/testtemplates",
                null,
                true,
                "classpath*:/io/github/furti/springangular/testtemplates/**/*.*"},
            {
                "/io/github/furti/springangular/testtemplates",
                "test",
                true,
                "classpath*:/io/github/furti/springangular/testtemplates/**/test*.*"}

        };
    }
}
