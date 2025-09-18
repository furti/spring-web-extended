package io.github.furti.spring.web.extended.io;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.Arrays;
import java.util.Locale;
import org.junit.jupiter.api.Test;

public class ResourceUtilsTest {

    @Test
    public void buildLocalizedTemplateNames() {
        assertThat(ResourceUtils.localizedResources(null, null), equalTo(null));
        assertThat(ResourceUtils.localizedResources(null, Locale.GERMAN), equalTo(null));
        assertThat(ResourceUtils.localizedResources("index", null), equalTo(Arrays.asList("index")));
        assertThat(
            ResourceUtils.localizedResources("index", new Locale("de")),
            equalTo(Arrays.asList("index_de", "index"))
        );
        assertThat(
            ResourceUtils.localizedResources("index.html", new Locale("de")),
            equalTo(Arrays.asList("index_de.html", "index.html"))
        );
        assertThat(
            ResourceUtils.localizedResources("index", new Locale("de", "AT")),
            equalTo(Arrays.asList("index_de-at", "index_de", "index"))
        );
    }

    @Test
    public void getNameAndEnding() {
        assertThat(ResourceUtils.getNameAndEnding(null), equalTo(null));
        assertThat(ResourceUtils.getNameAndEnding("test.jpg"), equalTo(new String[] { "test", ".jpg" }));
    }

    @Test
    public void pathAndFile() {
        assertThat(ResourceUtils.pathAndFile(null), equalTo(null));
        assertThat(ResourceUtils.pathAndFile(""), equalTo(new String[] { "", "" }));
        assertThat(ResourceUtils.pathAndFile("test"), equalTo(new String[] { "", "test" }));
        assertThat(ResourceUtils.pathAndFile("/test"), equalTo(new String[] { "/", "test" }));
        assertThat(ResourceUtils.pathAndFile("/abc/de/test"), equalTo(new String[] { "/abc/de/", "test" }));
    }

    @Test
    public void getLocaleFromName() {
        assertThat(ResourceUtils.getLocaleFromName(null), equalTo(null));
        assertThat(ResourceUtils.getLocaleFromName(""), equalTo(null));
        assertThat(ResourceUtils.getLocaleFromName("test"), equalTo(null));
        assertThat(ResourceUtils.getLocaleFromName("test-de"), equalTo(null));
        assertThat(ResourceUtils.getLocaleFromName("test_de"), equalTo("de"));
        assertThat(ResourceUtils.getLocaleFromName("test_de-AT"), equalTo("de-AT"));
    }

    @Test
    public void normalize() {
        assertThat(ResourceUtils.normalize(null, null), equalTo(null));
        assertThat(ResourceUtils.normalize("", ""), equalTo(null));
        assertThat(ResourceUtils.normalize("test", null), equalTo(null));
        assertThat(
            ResourceUtils.normalize("webjar:test/*/css/test.css", "../fonts/font.ff"),
            equalTo("webjar:test/*/fonts/font.ff")
        );
        assertThat(ResourceUtils.normalize("style/some.css", "../images/image.png"), equalTo("/images/image.png"));
        assertThat(ResourceUtils.normalize("/style/some.css", "../images/image.png"), equalTo("/images/image.png"));
    }
}
