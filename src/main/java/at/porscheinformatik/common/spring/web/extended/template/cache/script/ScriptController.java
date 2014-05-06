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
package at.porscheinformatik.common.spring.web.extended.template.cache.script;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import at.porscheinformatik.common.spring.web.extended.template.ResourceControllerBase;
import at.porscheinformatik.common.spring.web.extended.util.ResourceNotFoundException;

@Controller
@RequestMapping(value = "/**/script")
public class ScriptController extends ResourceControllerBase
{

	private ScriptStacks stacks;

	@RequestMapping(value = "/single/{stackId}/{scriptName}", method = RequestMethod.GET, produces = "text/javascript")
	@ResponseBody
	public String handleStylesheet(
			@PathVariable("stackId") String stackId,
			@PathVariable("scriptName") String scriptName,
			HttpServletResponse response)
	{
		if (stacks == null || !stacks.hasStack(stackId))
		{
			throw new ResourceNotFoundException();
		}

		ScriptStack stack = stacks.get(stackId);

		if (!stack.hasTemplate(scriptName))
		{
			throw new ResourceNotFoundException();
		}

		handleCaching(response, stack.isNoCaching());

		return stack.renderTemplate(scriptName);
	}

	@RequestMapping(value = "/stack/{stackId}", method = RequestMethod.GET, produces = "text/javascript")
	@ResponseBody
	public String handleStack(@PathVariable("stackId") String stackId,
			HttpServletResponse response)
	{
		if (stacks == null || !stacks.hasStack(stackId))
		{
			throw new ResourceNotFoundException();
		}

		ScriptStack stack = stacks.get(stackId);

		handleCaching(response, stack.isNoCaching());

		return stack.renderAll();
	}

	@Autowired
	public void setStacks(ScriptStacks stacks)
	{
		this.stacks = stacks;
	}
}
