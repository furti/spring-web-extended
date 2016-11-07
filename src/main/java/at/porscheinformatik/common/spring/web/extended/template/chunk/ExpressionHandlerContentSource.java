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
package at.porscheinformatik.common.spring.web.extended.template.chunk;

import com.x5.template.ContentSource;
import com.x5.template.Snippet;

import at.porscheinformatik.common.spring.web.extended.expression.ExpressionHandler;

/**
 * @author Daniel Furtlehner
 *
 */
public class ExpressionHandlerContentSource implements ContentSource
{
    private final String handlerName;
    private final ExpressionHandler expressionHandler;

    public ExpressionHandlerContentSource(String handlerName, ExpressionHandler expressionHandler)
    {
        super();
        this.handlerName = handlerName;
        this.expressionHandler = expressionHandler;
    }

    @Override
    public String fetch(String itemName)
    {
        return expressionHandler.process(itemName);
    }

    @Override
    public String getProtocol()
    {
        return handlerName;
    }

    @Override
    public boolean provides(String itemName)
    {
        return true;
    }

    @Override
    public Snippet getSnippet(String snippetName)
    {
        return null;
    }
}
