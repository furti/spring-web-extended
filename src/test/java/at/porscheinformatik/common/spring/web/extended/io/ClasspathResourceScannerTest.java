package at.porscheinformatik.common.spring.web.extended.io;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ClasspathResourceScannerTest
{

    @Test
    public void scanResources() throws IOException
    {
        ClasspathResourceScanner scanner = new ClasspathResourceScanner();

        Map<String, Resource> actual =
            scanner.scanResources("at/porscheinformatik/common/spring/web/extended/testtemplates/**/*", null);
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
            scanner.scanResources("at/porscheinformatik/common/spring/web/extended/testtemplates", "index.txt", false);

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
                "/at/porscheinformatik/common/springangular/testtemplates",
                "test.html",
                false,
                "classpath*:/at/porscheinformatik/common/springangular/testtemplates/test*.html"},
            {
                "/at/porscheinformatik/common/springangular/testtemplates",
                null,
                false,
                "classpath*:/at/porscheinformatik/common/springangular/testtemplates/*.*"},
            {
                "/at/porscheinformatik/common/springangular/testtemplates",
                "test",
                false,
                "classpath*:/at/porscheinformatik/common/springangular/testtemplates/test*.*"},
            {
                "/at/porscheinformatik/common/springangular/testtemplates",
                "test.html",
                true,
                "classpath*:/at/porscheinformatik/common/springangular/testtemplates/**/test*.html"},
            {
                "/at/porscheinformatik/common/springangular/testtemplates",
                null,
                true,
                "classpath*:/at/porscheinformatik/common/springangular/testtemplates/**/*.*"},
            {
                "/at/porscheinformatik/common/springangular/testtemplates",
                "test",
                true,
                "classpath*:/at/porscheinformatik/common/springangular/testtemplates/**/test*.*"}

        };
    }
}
