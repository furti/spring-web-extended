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
package io.github.furti.spring.web.extended.asset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Daniel Furtlehner
 *
 */
public class DefaultCdnConfig implements CdnConfig {

    private final Map<String, Map<String, CdnEntry>> stacks = new HashMap<>();

    @Override
    public CdnConfig addToStack(String stackName, String name, String location, String minifiedLocation) {
        if (!stacks.containsKey(stackName)) {
            stacks.put(stackName, new LinkedHashMap<String, CdnEntry>());
        }

        stacks.get(stackName).put(name, new CdnEntry(location, minifiedLocation));

        return this;
    }

    @Override
    public CdnConfig removeFromStack(String stackName, String name) {
        if (stacks.containsKey(stackName)) {
            stacks.get(stackName).remove(name);
        }

        return this;
    }

    @Override
    public CdnConfig removeStack(String stackName) {
        stacks.remove(stackName);
        return this;
    }

    @Override
    public List<CdnEntry> getEntries(String stackName) {
        Map<String, CdnEntry> stack = stacks.get(stackName);

        if (stack == null) {
            return null;
        }

        List<CdnEntry> entries = new ArrayList<>();

        for (CdnEntry entry : stack.values()) {
            entries.add(entry);
        }

        return entries;
    }
}
