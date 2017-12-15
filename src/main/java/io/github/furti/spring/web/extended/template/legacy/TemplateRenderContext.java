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
package io.github.furti.spring.web.extended.template.legacy;

import java.util.Locale;

import io.github.furti.spring.web.extended.io.ResourceType;

/**
 * In the optimized mode the templatecache caches templates for each templaterendercontext and reuses them when they are
 * requested for the same rendercontext again. So a template must not be rendered and processed by the optimizer chain
 * each time it is requested. This gives us better performance.
 *
 * <b>Be sure to only use data from the tempalterendercontext in your own expressionhandlers. Or else a cached template
 * may be used even though a expressoinhandler would have generated a different value for this request.</b>
 *
 * @author Daniel Furtlehner
 *
 */
public interface TemplateRenderContext
{
    Locale getLocale();

    ResourceType getResourceType();
}
