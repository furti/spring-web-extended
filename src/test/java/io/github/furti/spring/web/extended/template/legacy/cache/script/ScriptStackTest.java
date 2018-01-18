package io.github.furti.spring.web.extended.template.legacy.cache.script;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.i18n.LocaleContextHolder;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.github.furti.spring.web.extended.http.DefaultLinkCreator;
import io.github.furti.spring.web.extended.io.ClasspathResourceScanner;
import io.github.furti.spring.web.extended.io.ResourceScanner;
import io.github.furti.spring.web.extended.io.ResourceScanners;
import io.github.furti.spring.web.extended.template.legacy.DefaultTemplateRenderContextFactory;
import io.github.furti.spring.web.extended.template.legacy.cache.StackEntry;
import io.github.furti.spring.web.extended.template.legacy.cache.script.ScriptStack;
import io.github.furti.spring.web.extended.template.legacy.chunk.ChunkTemplateFactory;

public class ScriptStackTest
{

    @Test
    public void addResources() throws IOException
    {
        ScriptStack stack = buildStack();

        List<String> names = stack.getNames();

        assertThat(names.size(), equalTo(5));
        assertThat(names.get(0), equalTo("script1"));
        assertThat(names.get(1), equalTo("script2"));
        assertThat(names.get(2), equalTo("subscript"));
        assertThat(names.get(3), equalTo("subscript1"));
        assertThat(names.get(4), equalTo("localized"));
    }

    @Test(dataProvider = "renderAllData")
    public void renderAll(Locale locale, String expected) throws IOException
    {
        ScriptStack stack = buildStack();

        LocaleContextHolder.setLocale(locale);

        String renderAll = stack.renderAll();

        assertThat(renderAll, equalTo(expected));
    }

    @Test(dataProvider = "renderData")
    public void render(Locale locale, String name, String expected) throws IOException
    {
        ScriptStack stack = buildStack();
        LocaleContextHolder.setLocale(locale);

        String actual = stack.renderTemplate(name);

        assertThat(actual, equalTo(expected));
    }

    @DataProvider
    public Object[][] renderAllData()
    {
        return new Object[][]{
            {new Locale("de"), "content1\ncontent2\nsubcontent\nsubcontent1\nde\n"},
            {new Locale("en"), "content1\ncontent2\nsubcontent\nsubcontent1\nen\n"},
            {new Locale("fr"), "content1\ncontent2\nsubcontent\nsubcontent1\ndefaultlocale\n"}};
    }

    @DataProvider
    public Object[][] renderData()
    {
        return new Object[][]{
            // { new Locale("de"), "script1", "content1" },
            // { new Locale("en"), "script1", "content1" },
            // { new Locale("fr"), "script1", "content1" },
            // { new Locale("fr"), "localized", "defaultlocale" },
            // { new Locale("de"), "localized", "de" },
            // { new Locale("en"), "localized", "en" },
            {new Locale("de", "AT"), "localized", "de-at"}};
    }

    private ScriptStack buildStack() throws IOException
    {
        ScriptStack stack = new ScriptStack("default", false);
        stack.setScanners(buildScanners());
        ChunkTemplateFactory templateFactory = new ChunkTemplateFactory();
        templateFactory.buildTheme();
        stack.setTemplateFactory(templateFactory);
        stack.setTemplateRenderContextFactory(new DefaultTemplateRenderContextFactory());
        stack.setLinkCreator(new DefaultLinkCreator());

        stack.addResource("script1", StackEntry.resource(buildLocation("script1.js"), false));
        stack.addResource("script2", StackEntry.resource(buildLocation("script2.js"), false));
        stack.addResource("subscript", StackEntry.resource(buildLocation("sub/subscript.js"), false));
        stack.addResource("subscript1", StackEntry.resource(buildLocation("sub/script1.js"), false));
        stack.addResource("localized", StackEntry.resource(buildLocation("script-localized.js"), false));

        return stack;
    }

    private ResourceScanners buildScanners()
    {
        Map<String, ResourceScanner> scanners = new HashMap<>();
        scanners.put("classpath", new ClasspathResourceScanner());

        return new ResourceScanners(scanners);
    }

    private String buildLocation(String script)
    {
        return "classpath:io/github/furti/spring/web/extended/scripts/" + script;
    }
}