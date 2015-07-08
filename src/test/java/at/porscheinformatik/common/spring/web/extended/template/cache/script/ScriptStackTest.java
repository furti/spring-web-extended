package at.porscheinformatik.common.spring.web.extended.template.cache.script;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.i18n.LocaleContextHolder;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import at.porscheinformatik.common.spring.web.extended.io.ClasspathResourceScanner;
import at.porscheinformatik.common.spring.web.extended.io.ResourceScanner;
import at.porscheinformatik.common.spring.web.extended.io.ResourceScanners;
import at.porscheinformatik.common.spring.web.extended.template.DefaultTemplateRenderContextFactory;
import at.porscheinformatik.common.spring.web.extended.template.cache.StackEntry;
import at.porscheinformatik.common.spring.web.extended.template.chunk.ChunkTemplateFactory;

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
    public void render(Locale locale, String name, String expected)
        throws IOException
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
            {new Locale("de"),
                "content1\ncontent2\nsubcontent\nsubcontent1\nde\n"},
            {new Locale("en"),
                "content1\ncontent2\nsubcontent\nsubcontent1\nen\n"},
            {
                new Locale("fr"),
                "content1\ncontent2\nsubcontent\nsubcontent1\ndefaultlocale\n"}
        };
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
            {new Locale("de", "AT"), "localized", "de-at"}
        };
    }

    private ScriptStack buildStack() throws IOException
    {
        ScriptStack stack = new ScriptStack("default", false);
        stack.setScanners(buildScanners());
        ChunkTemplateFactory templateFactory = new ChunkTemplateFactory();
        templateFactory.buildTheme();
        stack.setTemplateFactory(templateFactory);
        stack.setTemplateRenderContextFactory(new DefaultTemplateRenderContextFactory());

        stack.addResource("script1",
            new StackEntry(buildLocation("script1.js"), false));
        stack.addResource("script2",
            new StackEntry(buildLocation("script2.js"), false));
        stack.addResource("subscript",
            new StackEntry(buildLocation("sub/subscript.js"), false));
        stack.addResource("subscript1",
            new StackEntry(buildLocation("sub/script1.js"), false));
        stack.addResource("localized",
            new StackEntry(buildLocation("script-localized.js"), false));

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
        return "classpath:at/porscheinformatik/common/spring/web/extended/scripts/"
            + script;
    }
}
