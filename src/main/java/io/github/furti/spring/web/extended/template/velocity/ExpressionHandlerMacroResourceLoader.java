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
package io.github.furti.spring.web.extended.template.velocity;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Set;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;
import org.springframework.util.CollectionUtils;

import io.github.furti.spring.web.extended.expression.ExpressionHandler;
import io.github.furti.spring.web.extended.expression.ExpressionHandlers;

/**
 * @author Daniel Furtlehner
 * @deprecated will be removed in a future version
 */
@Deprecated
public class ExpressionHandlerMacroResourceLoader extends ResourceLoader
{
    public static final String MACRO_LIBRARY_FILE = "spring-extended-vm-macro.vm";

    private final ExpressionHandlers expressionHandlers;

    public ExpressionHandlerMacroResourceLoader(ExpressionHandlers expressionHandlers)
    {
        super();
        this.expressionHandlers = expressionHandlers;
    }

    @Override
    public void init(ExtendedProperties configuration)
    {
    }

    @Override
    public InputStream getResourceStream(String source) throws ResourceNotFoundException
    {
        if (MACRO_LIBRARY_FILE.equals(source))
        {
            return IOUtils.toInputStream(buildMacros(), Charset.forName("UTF-8"));
        }
        else
        {
            return null;
        }
    }

    @Override
    public boolean isSourceModified(Resource resource)
    {
        return resource.isSourceModified();
    }

    @Override
    public long getLastModified(Resource resource)
    {
        return resource.getLastModified();
    }

    private String buildMacros()
    {
        Set<String> handlerNames = expressionHandlers.getHandlerNames();

        if (CollectionUtils.isEmpty(handlerNames))
        {
            return "";
        }

        StringBuilder macroBuilder = new StringBuilder();

        for (String handlerName : handlerNames)
        {
            ExpressionHandler handler = expressionHandlers.getHandler(handlerName);

            macroBuilder.append("#macro( ").append(handlerName);

            if (handler.valueNeeded())
            {
                macroBuilder.append(" $value )");
            }
            else
            {
                macroBuilder.append(" )");
            }

            macroBuilder.append("$expressionHandlers.processExpression( \"").append(handlerName).append("\" , ");
            if (handler.valueNeeded())
            {
                macroBuilder.append("$value )");
            }
            else
            {
                macroBuilder.append("'' )");
            }

            macroBuilder.append("#end\n");
        }

        return macroBuilder.toString();
    }
}
