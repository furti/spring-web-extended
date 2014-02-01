package at.porscheinformatik.common.springangular.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import at.porscheinformatik.common.springangular.resources.cache.script.ScriptStack;
import at.porscheinformatik.common.springangular.resources.cache.script.ScriptStacks;
import at.porscheinformatik.common.springangular.util.ResourceNotFoundException;

@Controller
@RequestMapping(value = "/**/script")
public class ScriptController
{

	private ScriptStacks stacks;

	@RequestMapping(value = "/single/{stackId}/{scriptName}", method = RequestMethod.GET, produces = "text/javascript")
	@ResponseBody
	public String handleStylesheet(
			@PathVariable("stackId") String stackId,
			@PathVariable("scriptName") String scriptName)
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

		return stack.renderTemplate(scriptName);
	}

	@RequestMapping(value = "/stack/{stackId}", method = RequestMethod.GET, produces = "text/javascript")
	@ResponseBody
	public String handleStack(@PathVariable("stackId") String stackId)
	{
		if (stacks == null || !stacks.hasStack(stackId))
		{
			throw new ResourceNotFoundException();
		}

		ScriptStack stack = stacks.get(stackId);

		return stack.renderAll();
	}

	@Autowired
	public void setStacks(ScriptStacks stacks)
	{
		this.stacks = stacks;
	}
}
