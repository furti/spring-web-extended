package at.porscheinformatik.common.springangular.template.cache;

public class StackEntry
{

	private String location;
	private String minifiedLocation;

	public StackEntry(String location, String minifiedLocation)
	{
		super();
		this.location = location;
		this.minifiedLocation = minifiedLocation;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public String getMinifiedLocation()
	{
		return minifiedLocation;
	}

	public void setMinifiedLocation(String minifiedLocation)
	{
		this.minifiedLocation = minifiedLocation;
	}
}
