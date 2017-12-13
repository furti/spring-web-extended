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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.Charset;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeConstants;
import org.springframework.core.io.Resource;

import io.github.furti.spring.web.extended.expression.ExpressionHandlers;
import io.github.furti.spring.web.extended.io.ResourceType;
import io.github.furti.spring.web.extended.template.BaseTemplate;

public class VelocityTemplate extends BaseTemplate
{

    private final VelocityEngine engine;
    private final Resource resource;
    private final ExpressionHandlers expressionHandlers;

    public VelocityTemplate(Resource resource, ResourceType type, String templateName, String location,
        boolean alreadyOptimized, VelocityEngine engine, ExpressionHandlers expressionHandlers) throws IOException
    {
        super(type, templateName, alreadyOptimized, location);
        this.engine = engine;
        this.resource = resource;
        this.expressionHandlers = expressionHandlers;
    }

    @Override
    protected String getContent() throws IOException
    {
        StringWriter writer = new StringWriter();

        try (Reader reader = new InputStreamReader(resource.getInputStream(),
            Charset.forName((String) engine.getProperty(RuntimeConstants.INPUT_ENCODING))))
        {
            engine.evaluate(buildContext(), writer, getName(), reader);
        }

        return writer.toString();
    }

    @Override
    protected void doRefresh() throws IOException
    {
        // Nothing to do here
    }

    @Override
    protected long getLastModified() throws IOException
    {
        return -1;
    }

    private Context buildContext()
    {
        VelocityContext context = new VelocityContext();

        context.put("expressionHandlers", expressionHandlers);

        /*
         * Velocity removes newlines after macros. If somebody wants to preserve
         * them we simply add a empty value.
         * 
         * we can write something like
         * 
         * #macro()$n other text
         * 
         * and the newline will be in the output
         */
        context.put("n", "");

        return context;
    }
}
