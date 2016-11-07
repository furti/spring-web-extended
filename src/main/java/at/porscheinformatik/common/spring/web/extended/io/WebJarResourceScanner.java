package at.porscheinformatik.common.spring.web.extended.io;

public class WebJarResourceScanner extends ClasspathShortcutResourceScanner
{
    public WebJarResourceScanner()
    {
        super("META-INF/resources/webjars/");
    }
}
