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
package at.porscheinformatik.common.spring.web.extended.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import at.porscheinformatik.common.spring.web.extended.expression.AssetExpressionHandler;
import at.porscheinformatik.common.spring.web.extended.expression.CdnExpressionHandler;
import at.porscheinformatik.common.spring.web.extended.expression.ExpressionHandler;
import at.porscheinformatik.common.spring.web.extended.expression.ExpressionHandlers;
import at.porscheinformatik.common.spring.web.extended.expression.InlineTemplateExpressionHandler;
import at.porscheinformatik.common.spring.web.extended.expression.MessageExpressionHandler;
import at.porscheinformatik.common.spring.web.extended.expression.ScriptExpressionHandler;
import at.porscheinformatik.common.spring.web.extended.expression.StyleExpressionHandler;

/**
 * @author Daniel Furtlehner
 *
 */
@Configuration
public class ExpressionHandlerConfig
{
    @Autowired
    private SpringWebExtendedConfigurerConfig configurerConfig;

    private HashMap<String, ExpressionHandler> handlers;

    @Bean
    public ExpressionHandlers expressionHandlers()
    {
        return new ExpressionHandlers(getExpressionHandlers());
    }

    @Bean
    public MessageExpressionHandler messageExpressionHandler()
    {
        return new MessageExpressionHandler();
    }

    @Bean
    public AssetExpressionHandler assetExpressionHandler()
    {
        return new AssetExpressionHandler();
    }

    @Bean
    public StyleExpressionHandler styleExpressionHandler()
    {
        return new StyleExpressionHandler(configurerConfig.getStyleConfig(), configurerConfig.appConfig());
    }

    @Bean
    public ScriptExpressionHandler scriptExpressionHandler()
    {
        return new ScriptExpressionHandler(configurerConfig.getScriptConfig(), configurerConfig.appConfig());
    }

    @Bean
    public InlineTemplateExpressionHandler inlineTemplateExpressionHandler()
    {
        return new InlineTemplateExpressionHandler();
    }

    @Bean
    public CdnExpressionHandler cdnExpressionHandler()
    {
        return new CdnExpressionHandler(configurerConfig.appConfig(), configurerConfig.getCdnConfig());
    }

    public Map<String, ExpressionHandler> getExpressionHandlers()
    {
        if (handlers == null)
        {
            handlers = new HashMap<>();
            handlers.put("message", messageExpressionHandler());
            handlers.put("asset", assetExpressionHandler());
            handlers.put("style", styleExpressionHandler());
            handlers.put("script", scriptExpressionHandler());
            handlers.put("asset", assetExpressionHandler());
            handlers.put("inlinetemplate", inlineTemplateExpressionHandler());
            handlers.put("cdn", cdnExpressionHandler());

            configurerConfig.configureExpressionHandlers(handlers);
        }

        return handlers;
    }
}
