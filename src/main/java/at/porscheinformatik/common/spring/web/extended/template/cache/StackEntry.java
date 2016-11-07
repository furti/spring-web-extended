package at.porscheinformatik.common.spring.web.extended.template.cache;

public class StackEntry
{

    public static StackEntry scan(String pattern, String basePath)
    {
        return new StackEntry(pattern, null, basePath, false, true);
    }

    public static StackEntry resource(String location, String minifiedLocation, boolean skipProcessing)
    {
        return new StackEntry(location, minifiedLocation, null, skipProcessing, false);
    }

    public static StackEntry resource(String location, boolean skipProcessing)
    {
        return new StackEntry(location, null, null, skipProcessing, false);
    }

    private final String location;
    private final String minifiedLocation;
    private final String basePath;
    private final boolean scanLocation;
    private final boolean skipProcessing;

    private StackEntry(String location, String minifiedLocation, String basePath, boolean skipProcessing,
        boolean scanLocation)
    {
        super();
        this.location = location;
        this.minifiedLocation = minifiedLocation;
        this.basePath = basePath;
        this.skipProcessing = skipProcessing;
        this.scanLocation = scanLocation;
    }

    public String getBasePath()
    {
        return basePath;
    }

    public String getLocation()
    {
        return location;
    }

    public String getMinifiedLocation()
    {
        return minifiedLocation;
    }

    public boolean isScanLocation()
    {
        return scanLocation;
    }

    public boolean isSkipProcessing()
    {
        return skipProcessing;
    }
}
