package at.porscheinformatik.common.springangular.expression.parser;

import org.parboiled.BaseParser;
import org.parboiled.Rule;

public class InlineTemplateParser extends BaseParser<Object>
{
	public Rule inlineTemplate()
	{
		return FirstOf(whiteSpace(),
				OneOrMore(whiteSpace(), text(), Optional(whiteSpace())),
				OneOrMore(text(), whiteSpace(), Optional(text())),
				text());
	}

	protected Rule text()
	{
		return Sequence(
				ZeroOrMore(Sequence(TestNot(AnyOf("\r\n")), ANY)),
				push(matchOrDefault("")));
	}

	protected Rule whiteSpace()
	{
		return Sequence(OneOrMore(AnyOf(" \t\r\n\f")),
				push(" "));
	}
}
