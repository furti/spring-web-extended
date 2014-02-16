package at.porscheinformatik.common.springangular.io;

public class WebJarResourceScanner extends ClasspathShortcutResourceScanner
{

	public WebJarResourceScanner()
	{
		super("META-INF/resources/webjars/");
	}
}
