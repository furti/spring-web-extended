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

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import at.porscheinformatik.common.spring.web.extended.io.ClasspathResourceScanner;
import at.porscheinformatik.common.spring.web.extended.io.ContextResourceScanner;
import at.porscheinformatik.common.spring.web.extended.io.ResourceScanner;
import at.porscheinformatik.common.spring.web.extended.io.ResourceScanners;
import at.porscheinformatik.common.spring.web.extended.io.WebJarResourceScanner;

/**
 * @author Daniel Furtlehner
 * 
 */
@Configuration
public class ResourceScannerConfig
{
	@Autowired
	private SpringWebExtendedConfigurerConfig configurerConfig;

	@Bean
	public ResourceScanners resourceScanners()
	{
		return new ResourceScanners(getScanners());
	}

	@Bean
	public ContextResourceScanner contextResourceScanner()
	{
		return new ContextResourceScanner();
	}

	@Bean
	public ClasspathResourceScanner classpathResourceScanner()
	{
		return new ClasspathResourceScanner();
	}

	@Bean
	public WebJarResourceScanner webJarResourceScanner()
	{
		return new WebJarResourceScanner();
	}

	public Map<String, ResourceScanner> getScanners()
	{
		Map<String, ResourceScanner> scanners = new HashMap<>();
		scanners.put("", contextResourceScanner());
		scanners.put("classpath", classpathResourceScanner());
		scanners.put("webjar", webJarResourceScanner());

		configurerConfig.configureResourceScanners(scanners);

		return scanners;
	}
}
