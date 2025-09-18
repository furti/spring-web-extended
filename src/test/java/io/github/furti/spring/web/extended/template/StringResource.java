package io.github.furti.spring.web.extended.template;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import org.apache.commons.io.input.ReaderInputStream;
import org.springframework.core.io.Resource;

public class StringResource implements Resource {

    private final String filename;
    private final String content;
    private InputStream stream;

    public StringResource(String string) {
        this(null, string);
    }

    public StringResource(String filename, String content) {
        this.filename = filename;
        this.content = content;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        stream = new ReaderInputStream(new StringReader(content), Charset.forName("UTF-8"));
        return stream;
    }

    @Override
    public boolean exists() {
        return content != null;
    }

    @Override
    public boolean isReadable() {
        return content != null;
    }

    @Override
    public boolean isOpen() {
        return stream != null;
    }

    @Override
    public URL getURL() throws IOException {
        return null;
    }

    @Override
    public URI getURI() throws IOException {
        return null;
    }

    @Override
    public File getFile() throws IOException {
        return null;
    }

    @Override
    public long contentLength() throws IOException {
        return content.length();
    }

    @Override
    public long lastModified() throws IOException {
        return 0;
    }

    @Override
    public Resource createRelative(String relativePath) throws IOException {
        return null;
    }

    @Override
    public String getFilename() {
        return filename;
    }

    @Override
    public String getDescription() {
        return content;
    }
}
