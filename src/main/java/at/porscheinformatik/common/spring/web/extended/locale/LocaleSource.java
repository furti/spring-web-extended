package at.porscheinformatik.common.spring.web.extended.locale;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Daniel Furtlehner
 *
 */
public interface LocaleSource
{

	Locale getLocale(HttpServletRequest request, HttpServletResponse response);
}
