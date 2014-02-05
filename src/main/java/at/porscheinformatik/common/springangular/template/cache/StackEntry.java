package at.porscheinformatik.common.springangular.template.cache;

public class StackEntry
{

	private String location;
	private String minifiedLocation;
	private boolean scanLocation;

	public StackEntry(String location, String minifiedLocation)
	{
		super();
		this.location = location;
		this.minifiedLocation = minifiedLocation;
		this.scanLocation = false;
	}

	public StackEntry(String location, boolean scanLocation)
	{
		this.location = location;
		this.scanLocation = scanLocation;
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

	public boolean isScanLocation()
	{
		return scanLocation;
	}

	public void setScanLocation(boolean scanLocation)
	{
		this.scanLocation = scanLocation;
	}
}
