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

import java.util.List;

/**
 * @author Daniel Furtlehner
 *
 */
public interface StackConfig {
    /**
     * If the intervall is less than 1 no refresh will be performed.
     *
     * @param intervall in seconds.
     * @return the stack config
     */
    StackConfig setRefreshIntervall(int intervall);

    /**
     * Defaults to 5 seconds
     *
     * @return intervall in seconds. -1 for no refresh
     */
    int getRefreshIntervall();

    StackConfig removeStack(String stackName);

    /**
     * equals to addToStack(stackName, resourceName, location, false)
     *
     * @param stackName
     * @param resourceName
     * @param location
     * @return the stack config
     */
    StackConfig addToStack(String stackName, String resourceName, String location);

    /**
     * @param stackName
     * @param resourceName
     * @param location
     * @param skipProcessing - if true the resource will be used as it is and no template processing is performed.
     *            Especially usefull for external scripts or styles that contains some special characters the template
     *            engine uses and that cant't be escaped by the application
     * @return the stack config
     */
    StackConfig addToStack(String stackName, String resourceName, String location, boolean skipProcessing);

    /**
     * equals to addToStack(stackName, resourceName, location, minifiedLocation, true)
     *
     * @param stackName
     * @param resourceName
     * @param location
     * @param minifiedLocation
     * @return the stack config
     */
    StackConfig addToStack(String stackName, String resourceName, String location, String minifiedLocation);

    /**
     *
     *
     * @param stackName
     * @param resourceName
     * @param location
     * @param minifiedLocation
     * @param skipProcessing
     * @return the stack config
     */
    StackConfig addToStack(
        String stackName,
        String resourceName,
        String location,
        String minifiedLocation,
        boolean skipProcessing
    );

    /**
     * Uses the given pattern to scan for resources.
     *
     * Valid Patterns are:
     * <ul>
     * <li>classpath:my/package/&#42;&#42;/&#42;</li>
     * <li>classpath:my/package/&#42;&#42;/&#42;.html</li>
     * <li>classpath:my/package&#42;</li>
     * </ul>
     *
     * @param stackName name of the stack to add the scanned resources to
     * @param pattern the pattern to scan for
     * @param basePath the base path
     * @return this config for a fluent api
     */
    StackConfig scanPattern(String stackName, String pattern, String basePath);

    default StackConfig scanPattern(String stackName, String pattern) {
        return scanPattern(stackName, pattern, null);
    }

    StackConfig removeFromStack(String stackName, String resourceName);

    /**
     * Disables caching for a cache. No cache headers will be written for each resource in the cache independent of the
     * optimizer resource profile.
     *
     * @param stackName - Name of the cache
     * @return config for further configurations
     */
    StackConfig noCaching(String stackName);

    boolean isNoCaching(String stackName);

    boolean hasStack(String stackName);

    List<String> getResourceNamesForStack(String stackName);
}
