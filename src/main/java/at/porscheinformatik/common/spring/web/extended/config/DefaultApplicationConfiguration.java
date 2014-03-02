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
package at.porscheinformatik.common.spring.web.extended.config;

public class DefaultApplicationConfiguration implements
		ApplicationConfiguration
{
	private String version;
	private boolean optimizeResources;

	@Override
	public String getVersion()
	{
		return version;
	}

	@Override
	public boolean isOptimizeResources()
	{
		return optimizeResources;
	}

	@Override
	public void setOptimizeResources(boolean optimizeResources)
	{
		this.optimizeResources = optimizeResources;
	}

	@Override
	public void setVersion(String version)
	{
		this.version = version;
	}
}
