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
package at.porscheinformatik.common.spring.web.extended.template.cache.style;

import at.porscheinformatik.common.spring.web.extended.template.cache.DefaultStackConfig;
import at.porscheinformatik.common.spring.web.extended.template.cache.StacksBase;

public class StyleStacks extends StacksBase<StyleStack>
{

    public StyleStacks(DefaultStackConfig config)
    {
        super(config);
    }

    @Override
    protected StyleStack createNewInstance(String stackName, boolean noCaching)
    {
        return new StyleStack(stackName, noCaching);
    }
}
