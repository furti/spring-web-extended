package io.github.furti.spring.web.extended.template.legacy.cache;

import java.util.HashMap;
import java.util.Map;

public class DefaultTemplateConfig implements TemplateConfig
{

    private int refreshIntervall = 0;
    private final Map<String, TemplateEntryConfig> config;

    public DefaultTemplateConfig()
    {
        config = new HashMap<>();
    }

    @Override
    public void setRefreshIntervall(int intervall)
    {
        refreshIntervall = intervall;
    }

    @Override
    public int getRefreshIntervall()
    {
        return refreshIntervall;
    }

    @Override
    public void addTemplateConfig(String name, TemplateEntryConfig config)
    {
        this.config.put(name, config);
    }

    @Override
    public Map<String, TemplateEntryConfig> getTemplateConfig()
    {
        return config;
    }
}
