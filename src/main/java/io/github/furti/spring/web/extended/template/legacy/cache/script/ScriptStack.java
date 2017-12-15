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
package io.github.furti.spring.web.extended.template.legacy.cache.script;

import io.github.furti.spring.web.extended.io.ResourceType;
import io.github.furti.spring.web.extended.template.legacy.cache.StackBase;

public class ScriptStack extends StackBase
{

    public ScriptStack(String stackName, boolean noCaching)
    {
        super(ResourceType.SCRIPT, stackName, noCaching);
    }

}
