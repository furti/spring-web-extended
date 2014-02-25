package at.porscheinformatik.common.spring.web.extended.io;

public class BootstrapCssResourceScanner extends ClasspathShortcutResourceScanner
{

	public BootstrapCssResourceScanner()
	{
		super("META-INF/resources/webjars/bootstrap/3.1.1/css/");
	}
}
