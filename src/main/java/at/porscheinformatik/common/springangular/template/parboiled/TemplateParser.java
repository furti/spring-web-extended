package at.porscheinformatik.common.springangular.template.parboiled;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.support.StringVar;
import org.springframework.util.Assert;

import at.porscheinformatik.common.springangular.expression.ExpressionHandlers;
import at.porscheinformatik.common.springangular.template.part.ExpressionPart;
import at.porscheinformatik.common.springangular.template.part.StringPart;

public class TemplateParser extends BaseParser<Object>
{
	protected final ExpressionHandlers handlers;
	protected final String expressionPrefix, expressionSuffix,
			expressionDelimiter;

	public TemplateParser(ExpressionHandlers handlers)
	{
		this(handlers, "${", "}", ":");
	}

	public TemplateParser(ExpressionHandlers handlers, String expressionPrefix,
			String expressionSuffix, String expressionDelimiter)
	{
		Assert.notNull(handlers, "Handlers must be set");
		Assert.notNull(expressionPrefix, "ExpressionPrefix must be set");
		Assert.notNull(expressionSuffix, "ExpressionSuffix must be set");
		Assert.notNull(expressionDelimiter, "ExpressionDelimiter must be set");
		this.handlers = handlers;
		this.expressionSuffix = expressionSuffix;
		this.expressionDelimiter = expressionDelimiter;
		this.expressionPrefix = expressionPrefix;
	}

	/**
	 * This rule matches a template that may contain any expressions wrapped in
	 * ${};
	 * 
	 * @return the rule for a template
	 */
	public Rule temlate()
	{
		/*
		 * The first rule of the following four that matches the template is
		 * used
		 * 
		 * 1. The template may start with an expression followed by static text
		 * and optionally end with an expression one or more times
		 * 
		 * 2. The template may start with static text followed by an expression
		 * and optionally end with static text one or more times
		 * 
		 * 3. the template contains one ore more expressions and not static text
		 * 
		 * 4. the template contains only static text
		 */
		return FirstOf(
				OneOrMore(expression(), staticText(), Optional(expression())),
				OneOrMore(staticText(), expression(), Optional(staticText())),
				OneOrMore(expression()),
				staticText());
	}

	protected Rule staticText()
	{
		return Sequence(
				ZeroOrMore(Sequence(TestNot(expressionPrefix), ANY)),
				push(new StringPart(matchOrDefault(""))));
	}

	protected Rule expression()
	{
		StringVar key = new StringVar("");
		StringVar value = new StringVar("");

		return Sequence(
				expressionPrefix,
				expressionKey(key),
				expressionDelimiter,
				expressionValue(value),
				expressionSuffix,
				push(new ExpressionPart(key.get(), value.get(), handlers)));
	}

	protected Rule expressionKey(StringVar key)
	{
		return Sequence(OneOrMore(
				Sequence(TestNot(expressionDelimiter), ANY)),
				key.set(matchOrDefault("")));
	}

	protected Rule expressionValue(StringVar value)
	{
		return Sequence(OneOrMore(
				Sequence(TestNot(expressionSuffix), ANY)),
				value.set(matchOrDefault("")));
	}
}
