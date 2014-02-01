package at.porscheinformatik.common.springangular.resources.cache;

import java.util.List;

public interface StackConfig
{

	/**
	 * If the intervall is less than 1 no refresh will be performed.
	 * 
	 * @param intervall
	 *            in seconds.
	 */
	void setRefreshIntervall(int intervall);

	/**
	 * Defaults to 5 seconds
	 * 
	 * @return intervall in seconds. -1 for no refresh
	 */
	int getRefreshIntervall();

	void removeStack(String stackName);

	void addToStack(String stackName, String resourceName, String location);

	void addToStack(String stackName, String resourceName, String location,
			String minifiedLocation);

	void removeFromStack(String stackName, String resourceName);

	boolean hasStack(String stackName);

	List<String> getResourceNamesForStack(String stackName);
}
