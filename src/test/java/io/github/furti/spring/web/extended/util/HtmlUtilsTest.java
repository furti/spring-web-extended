package io.github.furti.spring.web.extended.util;

import static io.github.furti.spring.web.extended.util.HtmlUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.junit.jupiter.api.Test;

public class HtmlUtilsTest
{

    @Test
    public void buildScriptLinkTest()
    {
        assertThat(buildScriptLink(null), equalTo("<script src=\"\" type=\"text/javascript\"></script>"));
        assertThat(buildScriptLink(""), equalTo("<script src=\"\" type=\"text/javascript\"></script>"));
        assertThat(buildScriptLink("test"), equalTo("<script src=\"test\" type=\"text/javascript\"></script>"));
        assertThat(buildScriptLink("test/test.js"),
            equalTo("<script src=\"test/test.js\" type=\"text/javascript\"></script>"));
    }

    @Test
    public void buildStyleLinkTest()
    {
        assertThat(buildStyleLink(null), equalTo("<link href=\"\" type=\"text/css\" rel=\"stylesheet\">"));
        assertThat(buildStyleLink(""), equalTo("<link href=\"\" type=\"text/css\" rel=\"stylesheet\">"));
        assertThat(buildStyleLink("test"), equalTo("<link href=\"test\" type=\"text/css\" rel=\"stylesheet\">"));
        assertThat(buildStyleLink("test/test.css"),
            equalTo("<link href=\"test/test.css\" type=\"text/css\" rel=\"stylesheet\">"));
    }
}
