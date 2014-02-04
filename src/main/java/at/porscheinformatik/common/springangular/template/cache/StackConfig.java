package at.porscheinformatik.common.springangular.template.cache;

import java.util.List;

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

	StackConfig addToStack(String stackName, String resourceName,
			String location);

	StackConfig addToStack(String stackName, String resourceName,
			String location,
			String minifiedLocation);

	StackConfig removeFromStack(String stackName, String resourceName);

	boolean hasStack(String stackName);

	List<String> getResourceNamesForStack(String stackName);
}
