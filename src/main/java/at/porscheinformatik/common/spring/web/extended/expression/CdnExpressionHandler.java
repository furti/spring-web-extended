/**
 * Copyright 2014 Daniel Furtlehner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.porscheinformatik.common.spring.web.extended.expression;

import java.util.List;

import org.springframework.util.Assert;

import at.porscheinformatik.common.spring.web.extended.asset.CdnConfig;
import at.porscheinformatik.common.spring.web.extended.asset.CdnEntry;
import at.porscheinformatik.common.spring.web.extended.config.ApplicationConfiguration;
import at.porscheinformatik.common.spring.web.extended.util.HtmlUtils;

public class CdnExpressionHandler extends UrlGeneratingExpressionHandler
{
    private final ApplicationConfiguration config;
    private final CdnConfig cdnConfig;

    public CdnExpressionHandler(ApplicationConfiguration config, CdnConfig cdnConfig)
    {
        super();
        Assert.notNull(config, "ApplicationConfiguration is required");
        Assert.notNull(cdnConfig, "CdnConfiguration is required");

        this.config = config;
        this.cdnConfig = cdnConfig;
    }

    @Override
    public String process(String value)
    {
        List<CdnEntry> entries = cdnConfig.getEntries(value);

        Assert.notNull(entries, "Stack " + value + " not found in CdnConfiguration");

        StringBuilder resourceLinks = new StringBuilder();

        for (CdnEntry entry : entries)
        {
            resourceLinks.append(buildLink(entry)).append("\n");
        }

        return resourceLinks.toString();
    }

    private String buildLink(CdnEntry entry)
    {
        String link = config.isOptimizeResources() ? entry.getMinifiedLocation() : entry.getLocation();

        int index = link.lastIndexOf(".");

        Assert.isTrue(index > 0, "Value for CDN must contain an ending [" + link + "]");

        String ending = link.substring(index + 1);

        if ("js".equals(ending))
        {
            return HtmlUtils.buildScriptLink(link);
        }
        else if ("css".equals(ending))
        {
            return HtmlUtils.buildStyleLink(link);
        }
        else
        {
            throw new IllegalArgumentException("Ending ." + ending + " not known");
        }
    }

    @Override
    public boolean valueNeeded()
    {
        return true;
    }
}
