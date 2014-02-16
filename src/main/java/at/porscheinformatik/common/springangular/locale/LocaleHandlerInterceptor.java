package at.porscheinformatik.common.springangular.locale;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.Assert;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LocaleHandlerInterceptor extends HandlerInterceptorAdapter
{

	private List<LocaleSource> sources;

	public LocaleHandlerInterceptor(List<LocaleSource> sources)
	{
		super();
		Assert.notEmpty(sources, "LocaleSources must not be empty");
		this.sources = sources;
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception
	{
		for (LocaleSource source : sources)
		{
			Locale locale = source.getLocale(request, response);

			if (locale != null)
			{
				LocaleContextHolder.setLocale(locale, true);
				return true;
			}
		}

		return true;
	}
}
