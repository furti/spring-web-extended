package io.github.furti.spring.web.extended.template.legacy.cache;

public class DefaultTemplateEntryConfig implements TemplateEntryConfig
{

    private String locationPrefix;

    public DefaultTemplateEntryConfig(String locationPrefix)
    {
        super();
        this.locationPrefix = locationPrefix;
    }

    @Override
    public String getLocationPrefix()
    {
        return locationPrefix;
    }

    public void setLocationPrefix(String locationPrefix)
    {
        this.locationPrefix = locationPrefix;
    }
}
