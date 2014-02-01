package at.porscheinformatik.common.springangular.resources.cache;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import at.porscheinformatik.common.springangular.resources.StackEntry;

public class DefaultStackConfig implements StackConfig
{
	private int refreshIntervall;
	private LinkedHashMap<String, LinkedHashMap<String, StackEntry>> stacks = new LinkedHashMap<>();

	public DefaultStackConfig()
	{
		super();
	}

	@Override
	public void setRefreshIntervall(int intervall)
	{
		this.refreshIntervall = intervall;
	}

	@Override
	public int getRefreshIntervall()
	{
		return refreshIntervall;
	}

	@Override
	public void removeStack(String stackName)
	{
		stacks.remove(stackName);
	}

	@Override
	public void addToStack(String stackName, String resourceName,
			String location)
	{
		addToStack(stackName, resourceName, location, null);
	}

	@Override
	public void removeFromStack(String stackName, String resourceName)
	{
		if (!hasStack(stackName))
		{
			return;
		}

		stacks.get(stackName).remove(resourceName);
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
	public void addToStack(String stackName, String resourceName,
			String location, String minifiedLocation)
	{
		if (!hasStack(stackName))
		{
			stacks.put(stackName, new LinkedHashMap<String, StackEntry>());
		}

		stacks.get(stackName).put(resourceName,
				new StackEntry(location, minifiedLocation));
	}
}
