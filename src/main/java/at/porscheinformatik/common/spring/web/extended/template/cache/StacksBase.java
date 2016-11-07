/**
 * Copyright 2014 Daniel Furtlehner Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package at.porscheinformatik.common.spring.web.extended.template.cache;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import at.porscheinformatik.common.spring.web.extended.config.ApplicationConfiguration;
import at.porscheinformatik.common.spring.web.extended.http.LinkCreator;
import at.porscheinformatik.common.spring.web.extended.io.ResourceScanners;
import at.porscheinformatik.common.spring.web.extended.template.TemplateFactory;
import at.porscheinformatik.common.spring.web.extended.template.TemplateRenderContextFactory;
import at.porscheinformatik.common.spring.web.extended.template.cache.style.StyleStacks;
import at.porscheinformatik.common.spring.web.extended.template.optimize.OptimizerChain;

public abstract class StacksBase<T extends StackBase>
{

    private static final Logger LOG = LoggerFactory.getLogger(StyleStacks.class);

    private LinkedHashMap<String, T> stacks;
    private ResourceScanners scanners;
    private TemplateFactory templateFactory;
    private TemplateRenderContextFactory templateRenderContextFactory;
    private OptimizerChain optimizerChain;
    private LinkCreator linkCreator;

    private final DefaultStackConfig config;

    private ApplicationConfiguration appConfig;

    public StacksBase(DefaultStackConfig config)
    {
        super();
        this.config = config;
    }

    public boolean hasStack(String stackId)
    {
        return stacks != null && stacks.containsKey(stackId);
    }

    public T get(String stackId)
    {
        return stacks != null ? stacks.get(stackId) : null;
    }

    @PostConstruct
    private void setupStacks() throws IOException
    {
        stacks = new LinkedHashMap<>();
        Map<String, LinkedHashMap<String, StackEntry>> stackConfigs = config.getStacks();

        if (stackConfigs == null)
        {
            return;
        }

        for (Entry<String, LinkedHashMap<String, StackEntry>> entry : stackConfigs.entrySet())
        {
            T stack = createNewInstance(entry.getKey(), config.isNoCaching(entry.getKey()));
            stack.setTemplateFactory(templateFactory);
            stack.setScanners(scanners);
            stack.setOptimizerChain(optimizerChain);
            stack.setAppConfig(appConfig);
            stack.setLinkCreator(linkCreator);
            stack.setTemplateRenderContextFactory(templateRenderContextFactory);

            for (Entry<String, StackEntry> resourceEntry : entry.getValue().entrySet())
            {
                stack.addResource(resourceEntry.getKey(), resourceEntry.getValue());
            }

            stacks.put(entry.getKey(), stack);
        }
    }

    protected abstract T createNewInstance(String stackName, boolean noCaching);

    public void refresh()
    {
        if (stacks == null)
        {
            return;
        }

        for (T stack : stacks.values())
        {
            try
            {
                stack.refreshTemplates();
            }
            catch (IOException ex)
            {
                LOG.error("Error refreshing styles", ex);
            }
        }
    }

    public void setScanners(ResourceScanners scanners)
    {
        this.scanners = scanners;
    }

    public void setOptimizerChain(OptimizerChain optimizerChain)
    {
        this.optimizerChain = optimizerChain;
    }

    public void setAppConfig(ApplicationConfiguration appConfig)
    {
        this.appConfig = appConfig;
    }

    @Autowired
    public void setTemplateFactory(TemplateFactory templateFactory)
    {
        this.templateFactory = templateFactory;
    }

    @Autowired
    public void setLinkCreator(LinkCreator linkCreator)
    {
        this.linkCreator = linkCreator;
    }

    @Autowired
    public void setTemplateRenderContextFactory(TemplateRenderContextFactory templateRenderContextFactory)
    {
        this.templateRenderContextFactory = templateRenderContextFactory;
    }
}
