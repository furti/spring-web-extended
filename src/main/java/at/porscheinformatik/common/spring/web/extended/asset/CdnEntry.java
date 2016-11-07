package at.porscheinformatik.common.spring.web.extended.asset;

public class CdnEntry
{

    private final String location;
    private final String minifiedLocation;

    public CdnEntry(String location, String minifiedLocation)
    {
        super();
        this.location = location;
        this.minifiedLocation = minifiedLocation;
    }

    public String getLocation()
    {
        return location;
    }

    public String getMinifiedLocation()
    {
        return minifiedLocation;
    }
}
