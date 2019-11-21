package io.github.furti.spring.web.extended.io;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;

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

    @Test
    public void buildScanPath()
    {
        performBuildScanPath("/io/github/furti/springangular/testtemplates", "test.html", false,
            "classpath*:/io/github/furti/springangular/testtemplates/test*.html");
        performBuildScanPath("/io/github/furti/springangular/testtemplates", null, false,
            "classpath*:/io/github/furti/springangular/testtemplates/*.*");
        performBuildScanPath("/io/github/furti/springangular/testtemplates", "test", false,
            "classpath*:/io/github/furti/springangular/testtemplates/test*.*");
        performBuildScanPath("/io/github/furti/springangular/testtemplates", "test.html", true,
            "classpath*:/io/github/furti/springangular/testtemplates/**/test*.html");
        performBuildScanPath("/io/github/furti/springangular/testtemplates", null, true,
            "classpath*:/io/github/furti/springangular/testtemplates/**/*.*");
        performBuildScanPath("/io/github/furti/springangular/testtemplates", "test", true,
            "classpath*:/io/github/furti/springangular/testtemplates/**/test*.*");
    }

    private void performBuildScanPath(String path, String file, boolean scanSubDirectories, String expected)
    {
        ClasspathResourceScanner scanner = new ClasspathResourceScanner();

        String actual = scanner.buildScanPath(path, file, scanSubDirectories);

        assertThat(actual, equalTo(expected));
    }
}
