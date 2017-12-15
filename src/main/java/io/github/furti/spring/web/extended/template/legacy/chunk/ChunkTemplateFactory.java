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
package io.github.furti.spring.web.extended.template.legacy.chunk;

import java.io.IOException;
import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;

import com.x5.template.Theme;

import io.github.furti.spring.web.extended.expression.legacy.ExpressionHandler;
import io.github.furti.spring.web.extended.expression.legacy.ExpressionHandlers;
import io.github.furti.spring.web.extended.io.ResourceType;
import io.github.furti.spring.web.extended.template.legacy.Template;
import io.github.furti.spring.web.extended.template.legacy.TemplateFactory;

public class ChunkTemplateFactory implements TemplateFactory
{
    private ExpressionHandlers expressionHandlers;
    private Theme theme;

    @Override
    public Template createTemplate(Resource resource, String templateName, String location, ResourceType type,
        boolean optimized)
    {
        try
        {
            ChunkTemplate template = new ChunkTemplate(type, templateName, location, optimized, resource, theme);
            template.refresh();

            return template;
        }
        catch (IOException ex)
        {
            throw new RuntimeException("Error creating ChunkTemplate for template " + templateName, ex);
        }
    }

    @PostConstruct
    public void buildTheme()
    {
        theme = new Theme();

        if (expressionHandlers == null)
        {
            return;
        }

        Collection<String> expressionHandlerNames = expressionHandlers.getHandlerNames();

        if (CollectionUtils.isEmpty(expressionHandlerNames))
        {
            return;
        }

        for (String handlerName : expressionHandlerNames)
        {
            ExpressionHandler handler = expressionHandlers.getHandler(handlerName);

            theme.addProtocol(new ExpressionHandlerContentSource(handlerName, handler));
        }
    }

    @Autowired
    public void setExpressionHandlers(ExpressionHandlers expressionHandlers)
    {
        this.expressionHandlers = expressionHandlers;
    }
}
