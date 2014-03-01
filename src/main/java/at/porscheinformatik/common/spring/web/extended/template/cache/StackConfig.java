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
package at.porscheinformatik.common.spring.web.extended.template.cache;

import java.util.List;

/**
 * @author Daniel Furtlehner
 * 
 */
public interface StackConfig
{

	/**
	 * If the intervall is less than 1 no refresh will be performed.
	 * 
	 * @param intervall
	 *            in seconds.
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
	 * @return
	 */
	StackConfig addToStack(String stackName, String resourceName,
			String location);

	/**
	 * @param stackName
	 * @param resourceName
	 * @param location
	 * @param skipProcessing
	 *            - if true the resource will be used as it is and no template
	 *            processing is performed. Especially usefull for external
	 *            scripts or styles that contains some special characters the
	 *            template engine uses and that cant't be escaped by the
	 *            application
	 * @return
	 */
	StackConfig addToStack(String stackName, String resourceName,
			String location, boolean skipProcessing);

	/**
	 * equals to addToStack(stackName, resourceName, location, minifiedLocation,
	 * true)
	 * 
	 * @param stackName
	 * @param resourceName
	 * @param location
	 * @param minifiedLocation
	 * @return
	 */
	StackConfig addToStack(String stackName, String resourceName,
			String location,
			String minifiedLocation);

	/**
	 * 
	 * 
	 * @param stackName
	 * @param resourceName
	 * @param location
	 * @param minifiedLocation
	 * @param skipProcessing
	 * @return
	 */
	StackConfig addToStack(String stackName, String resourceName,
			String location,
			String minifiedLocation, boolean skipProcessing);

	/**
	 * Scans all resources in a path and adds them to the stack. The ordering of
	 * the resources is unspecified. Especially usefull for html template
	 * scanning where the ordering of the resources does not matter
	 * 
	 * @param stackName
	 * @param path
	 * @return
	 */
	StackConfig scanPath(String stackName, String path);

	StackConfig removeFromStack(String stackName, String resourceName);

	boolean hasStack(String stackName);

	List<String> getResourceNamesForStack(String stackName);
}
