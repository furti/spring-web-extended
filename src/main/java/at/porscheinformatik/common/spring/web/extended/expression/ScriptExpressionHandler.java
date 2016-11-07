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

import at.porscheinformatik.common.spring.web.extended.config.ApplicationConfiguration;
import at.porscheinformatik.common.spring.web.extended.template.cache.StackConfig;
import at.porscheinformatik.common.spring.web.extended.util.HtmlUtils;

public class ScriptExpressionHandler extends UrlGeneratingExpressionHandler
{
    private final StackConfig scriptConfig;
    private final ApplicationConfiguration config;

    public ScriptExpressionHandler(StackConfig scriptConfig, ApplicationConfiguration config)
    {
        super();
        this.scriptConfig = scriptConfig;
        this.config = config;
    }

    @Override
    public String process(String value)
    {
        Assert.notNull(scriptConfig, "No scriptstack defined");
        Assert.isTrue(scriptConfig.hasStack(value), "ScriptStack " + value + " not found");

        if (config.isOptimizeResources())
        {
            return HtmlUtils.buildScriptLink(generateUrl("script/stack", value));
        }
        else
        {
            return buildDevelopmentScripts(value);
        }
    }

    private String buildDevelopmentScripts(String stackName)
    {
        List<String> scriptNames = scriptConfig.getResourceNamesForStack(stackName);

        Assert.notEmpty(scriptNames, "No script defined in stack " + stackName);

        StringBuilder builder = new StringBuilder();

        for (String styleName : scriptNames)
        {
            builder.append(HtmlUtils.buildScriptLink(generateUrl("script/single", stackName, styleName))).append("\n");
        }

        return builder.toString();
    }

    @Override
    public boolean valueNeeded()
    {
        return true;
    }
}
