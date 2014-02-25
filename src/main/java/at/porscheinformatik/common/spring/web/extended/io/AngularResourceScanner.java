package at.porscheinformatik.common.spring.web.extended.io;

public class AngularResourceScanner extends ClasspathShortcutResourceScanner
{
	public AngularResourceScanner()
	{
		super("META-INF/resources/webjars/angularjs/1.2.12/");
	}
}