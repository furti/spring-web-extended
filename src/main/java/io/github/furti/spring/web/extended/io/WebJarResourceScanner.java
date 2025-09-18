package io.github.furti.spring.web.extended.io;

public class WebJarResourceScanner extends ClasspathShortcutResourceScanner {

    public WebJarResourceScanner() {
        super("META-INF/resources/webjars/");
    }
}
