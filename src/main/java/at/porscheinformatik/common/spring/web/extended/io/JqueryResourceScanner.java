package at.porscheinformatik.common.spring.web.extended.io;

public class JqueryResourceScanner extends ClasspathShortcutResourceScanner
{

	public JqueryResourceScanner()
	{
		super("META-INF/resources/webjars/jquery/2.1.0/");
	}
}
