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
package io.github.furti.spring.web.extended.template.legacy.cache;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * @author Daniel Furtlehner
 *
 */
public class DefaultStackConfig implements StackConfig {

    private int refreshIntervall;
    private final LinkedHashMap<String, LinkedHashMap<String, StackEntry>> stacks = new LinkedHashMap<>();
    private final Set<String> noCachingStacks = new HashSet<>();

    public DefaultStackConfig() {
        super();
    }

    @Override
    public StackConfig setRefreshIntervall(int intervall) {
        refreshIntervall = intervall;

        return this;
    }

    @Override
    public int getRefreshIntervall() {
        return refreshIntervall;
    }

    @Override
    public StackConfig removeStack(String stackName) {
        stacks.remove(stackName);

        return this;
    }

    @Override
    public StackConfig addToStack(String stackName, String resourceName, String location) {
        addToStack(stackName, resourceName, location, null);

        return this;
    }

    @Override
    public StackConfig addToStack(String stackName, String resourceName, String location, boolean skipProcessing) {
        return addToStack(stackName, resourceName, location, null, skipProcessing);
    }

    @Override
    public StackConfig removeFromStack(String stackName, String resourceName) {
        if (!hasStack(stackName)) {
            return this;
        }

        stacks.get(stackName).remove(resourceName);

        return this;
    }

    @Override
    public boolean hasStack(String stackName) {
        return stacks.containsKey(stackName);
    }

    @Override
    public List<String> getResourceNamesForStack(String stackName) {
        if (!hasStack(stackName)) {
            return null;
        }

        return new ArrayList<>(stacks.get(stackName).keySet());
    }

    public LinkedHashMap<String, LinkedHashMap<String, StackEntry>> getStacks() {
        return stacks;
    }

    @Override
    public StackConfig addToStack(String stackName, String resourceName, String location, String minifiedLocation) {
        return addToStack(stackName, resourceName, location, minifiedLocation, false);
    }

    @Override
    public StackConfig addToStack(
        String stackName,
        String resourceName,
        String location,
        String minifiedLocation,
        boolean skipProcessing
    ) {
        if (!hasStack(stackName)) {
            stacks.put(stackName, new LinkedHashMap<String, StackEntry>());
        }

        stacks.get(stackName).put(resourceName, StackEntry.resource(location, minifiedLocation, skipProcessing));

        return this;
    }

    @Override
    public StackConfig scanPattern(String stackName, String pattern, String basePath) {
        if (!hasStack(stackName)) {
            stacks.put(stackName, new LinkedHashMap<String, StackEntry>());
        }

        stacks.get(stackName).put(pattern, StackEntry.scan(pattern, basePath));

        return this;
    }

    @Override
    public StackConfig noCaching(String stackName) {
        noCachingStacks.add(stackName);

        return this;
    }

    @Override
    public boolean isNoCaching(String stackName) {
        return noCachingStacks.contains(stackName);
    }
}
