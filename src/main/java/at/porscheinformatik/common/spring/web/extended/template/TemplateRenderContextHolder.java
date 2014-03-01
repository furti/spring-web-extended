package at.porscheinformatik.common.spring.web.extended.template;

public class TemplateRenderContextHolder
{

	private static final ThreadLocal<TemplateRenderContext> CONTEXT = new ThreadLocal<>();

	private TemplateRenderContextHolder()
	{
	}

	public static TemplateRenderContext actualContext()
	{
		return CONTEXT.get();
	}

	public static void setContext(TemplateRenderContext context)
	{
		CONTEXT.set(context);
	}
}
