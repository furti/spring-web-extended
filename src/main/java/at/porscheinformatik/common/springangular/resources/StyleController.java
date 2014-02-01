package at.porscheinformatik.common.springangular.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import at.porscheinformatik.common.springangular.resources.cache.style.StyleStack;
import at.porscheinformatik.common.springangular.resources.cache.style.StyleStacks;
import at.porscheinformatik.common.springangular.util.ResourceNotFoundException;

@Controller
@RequestMapping(value = "/**/style")
public class StyleController
{

	private StyleStacks stacks;

	@RequestMapping(value = "/single/{stackId}/{styleName}", method = RequestMethod.GET, produces = "text/css")
	@ResponseBody
	public String handleStylesheet(
			@PathVariable("stackId") String stackId,
			@PathVariable("styleName") String styleName)
	{
		if (stacks == null || !stacks.hasStack(stackId))
		{
			throw new ResourceNotFoundException();
		}

		StyleStack stack = stacks.get(stackId);

		if (!stack.hasTemplate(styleName))
		{
			throw new ResourceNotFoundException();
		}

		return stack.renderTemplate(styleName);
	}

	@RequestMapping(value = "/stack/{stackId}", method = RequestMethod.GET, produces = "text/css")
	@ResponseBody
	public String handleStack(@PathVariable("stackId") String stackId)
	{
		if (stacks == null || !stacks.hasStack(stackId))
		{
			throw new ResourceNotFoundException();
		}

		StyleStack stack = stacks.get(stackId);

		// TODO: is it a good idea to combine all styles in the stack? IE css
		// file size limit?
		return stack.renderAll();
	}

	@Autowired
	public void setStacks(StyleStacks stacks)
	{
		this.stacks = stacks;
	}
}
