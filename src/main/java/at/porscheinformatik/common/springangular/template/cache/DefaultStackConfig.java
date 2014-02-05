package at.porscheinformatik.common.springangular.template.cache;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class DefaultStackConfig implements StackConfig
{
	private int refreshIntervall;
	private LinkedHashMap<String, LinkedHashMap<String, StackEntry>> stacks = new LinkedHashMap<>();

	public DefaultStackConfig()
	{
		super();
	}

	@Override
	public StackConfig setRefreshIntervall(int intervall)
	{
		this.refreshIntervall = intervall;

		return this;
	}

	@Override
	public int getRefreshIntervall()
	{
		return refreshIntervall;
	}

	@Override
	public StackConfig removeStack(String stackName)
	{
		stacks.remove(stackName);

		return this;
	}

	@Override
	public StackConfig addToStack(String stackName, String resourceName,
			String location)
	{
		addToStack(stackName, resourceName, location, null);

		return this;
	}

	@Override
	public StackConfig removeFromStack(String stackName, String resourceName)
	{
		if (!hasStack(stackName))
		{
			return this;
		}

		stacks.get(stackName).remove(resourceName);

		return this;
	}

	@Override
	public boolean hasStack(String stackName)
	{
		return stacks.containsKey(stackName);
	}

	@Override
	public List<String> getResourceNamesForStack(String stackName)
	{
		if (!hasStack(stackName))
		{
			return null;
		}

		return new ArrayList<String>(stacks.get(stackName).keySet());
	}

	public LinkedHashMap<String, LinkedHashMap<String, StackEntry>> getStacks()
	{
		return stacks;
	}

	@Override
	public StackConfig addToStack(String stackName, String resourceName,
			String location, String minifiedLocation)
	{
		if (!hasStack(stackName))
		{
			stacks.put(stackName, new LinkedHashMap<String, StackEntry>());
		}

		stacks.get(stackName).put(resourceName,
				new StackEntry(location, minifiedLocation));

		return this;
	}

	@Override
	public StackConfig scanPath(String stackName, String path)
	{
		if (!hasStack(stackName))
		{
			stacks.put(stackName, new LinkedHashMap<String, StackEntry>());
		}

		stacks.get(stackName).put(path, new StackEntry(path, true));

		return this;
	}
}
