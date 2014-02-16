package at.porscheinformatik.common.springangular.io;

public class BootstrapCssResourceScanner extends ClasspathShortcutResourceScanner
{

	public BootstrapCssResourceScanner()
	{
		super("META-INF/resources/webjars/bootstrap/3.1.1/css/");
	}
}
