package at.porscheinformatik.common.springangular.resources;

public enum ResourceType
{
	STYLE("text/css"), SCRIPT("text/javascript"), TEMPLATE("text/html");

	private String contentType;

	private ResourceType(String contentType)
	{
		this.contentType = contentType;
	}

	public String getContentType()
	{
		return contentType;
	}
}
