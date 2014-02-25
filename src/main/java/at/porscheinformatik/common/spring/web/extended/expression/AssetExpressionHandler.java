package at.porscheinformatik.common.spring.web.extended.expression;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import at.porscheinformatik.common.spring.web.extended.util.SpringAngularUtils;

/**
 * Produces a realtive url for an asset
 * 
 * @author Daniel Furtlehner
 * 
 */
public class AssetExpressionHandler implements ExpressionHandler
{

	private LocaleContext locale;

	@Override
	public String process(String value)
	{
		String[] split = SpringAngularUtils.parseExpression(value);
		Assert.isTrue(split.length == 2, "Invalid asset " + value);

		StringBuilder url = new StringBuilder();

		// Append the loacle first. Else localized assets may be cached by the
		// browser for the wrong locale
		url.append(locale.getLocale().toString())
				.append("/asset/");

		if (StringUtils.hasText(split[0]))
		{
			url.append(split[0]).append("/");
		}
		else
		{
			url.append("context").append("/");
		}

		url.append(split[1]);

		return url.toString();
	}

	@Autowired
	public void setLocale(LocaleContext locale)
	{
		this.locale = locale;
	}
}
