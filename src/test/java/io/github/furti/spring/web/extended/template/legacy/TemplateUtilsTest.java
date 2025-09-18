package io.github.furti.spring.web.extended.template.legacy;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import io.github.furti.spring.web.extended.template.legacy.cache.CacheUtils;
import org.junit.jupiter.api.Test;

public class TemplateUtilsTest {

    @Test
    public void buildPath() {
        performbuildPath(null, null, "");
        performbuildPath(null, "test", "test");
        performbuildPath(null, "test.html", "test");
        performbuildPath("", "test.html", "test");
        performbuildPath("abc", "test.html", "abc/test");
    }

    private void performbuildPath(String configName, String path, String expected) {
        String actual = CacheUtils.buildPath(configName, path);

        assertThat(actual, equalTo(expected));
    }
}
