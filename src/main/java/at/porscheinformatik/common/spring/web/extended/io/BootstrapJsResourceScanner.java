package at.porscheinformatik.common.spring.web.extended.io;

public class BootstrapJsResourceScanner extends ClasspathShortcutResourceScanner
{

	public BootstrapJsResourceScanner()
	{
		super("META-INF/resources/webjars/bootstrap/3.1.1/js/");
	}
}
