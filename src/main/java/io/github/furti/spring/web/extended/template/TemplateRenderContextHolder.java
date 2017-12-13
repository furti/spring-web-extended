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
package io.github.furti.spring.web.extended.template;

import java.util.ArrayList;
import java.util.List;

public class TemplateRenderContextHolder
{
    /*
     * We need more than one context per thread. For example if we have a script
     * file, that contains a inlinetemplate, the script file needs a context
     * with a resourcetype of text/javascript and the inlinetemplate needs the
     * type text/html.
     */
    private static final ThreadLocal<List<TemplateRenderContext>> CONTEXT = new ThreadLocal<>();

    private TemplateRenderContextHolder()
    {
    }

    public static TemplateRenderContext actualContext()
    {
        List<TemplateRenderContext> list = CONTEXT.get();

        if (list == null || list.size() == 0)
        {
            return null;
        }

        return list.get(list.size() - 1);
    }

    public static void setCurrentContext(TemplateRenderContext context)
    {
        if (CONTEXT.get() == null)
        {
            CONTEXT.set(new ArrayList<TemplateRenderContext>());
        }

        CONTEXT.get().add(context);
    }

    public static void removeCurrentContext()
    {
        List<TemplateRenderContext> list = CONTEXT.get();

        if (list == null)
        {
            return;
        }

        if (list.size() > 0)
        {
            list.remove(list.size() - 1);
        }

        if (list.size() == 0)
        {
            CONTEXT.set(null);
        }
    }
}
