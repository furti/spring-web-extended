package io.github.furti.spring.web.extended.template.legacy;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.commons.io.input.ReaderInputStream;
import org.springframework.core.io.Resource;

public class StringResource implements Resource
{
    private final String string;
    private InputStream stream;

    public StringResource(String string)
    {
        super();
        this.string = string;
    }

    @Override
    public InputStream getInputStream() throws IOException
    {
        stream = new ReaderInputStream(new StringReader(string), Charset.forName("UTF-8"));
        return stream;
    }

    @Override
    public boolean exists()
    {
        return string != null;
    }

    @Override
    public boolean isReadable()
    {
        return string != null;
    }

    @Override
    public boolean isOpen()
    {
        return stream != null;
    }

    @Override
    public URL getURL() throws IOException
    {
        return null;
    }

    @Override
    public URI getURI() throws IOException
    {
        return null;
    }

    @Override
    public File getFile() throws IOException
    {
        return null;
    }

    @Override
    public long contentLength() throws IOException
    {
        return string.length();
    }

    @Override
    public long lastModified() throws IOException
    {
        return 0;
    }

    @Override
    public Resource createRelative(String relativePath) throws IOException
    {
        return null;
    }

    @Override
    public String getFilename()
    {
        return null;
    }

    @Override
    public String getDescription()
    {
        return string;
    }

}
