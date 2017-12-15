package io.github.furti.spring.web.extended.template.legacy;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;

import io.github.furti.spring.web.extended.io.ResourceType;

public class StringTemplate extends BaseTemplate
{
    private final Resource resource;
    private String content;

    public StringTemplate(ResourceType type, String templateName, boolean alreadyOptimized, Resource resource,
        String location) throws IOException
    {
        super(type, templateName, alreadyOptimized, location);
        this.resource = resource;
        doRefresh();
    }

    @Override
    protected String getContent() throws IOException
    {
        return content;
    }

    @Override
    protected void doRefresh() throws IOException
    {
        try (Reader reader = new InputStreamReader(resource.getInputStream()))
        {
            content = IOUtils.toString(reader);
        }
    }

    @Override
    protected long getLastModified() throws IOException
    {
        return resource.lastModified();
    }

}
