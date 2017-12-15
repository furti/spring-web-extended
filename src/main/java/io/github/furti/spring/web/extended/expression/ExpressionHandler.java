/**
 * 
 */
package io.github.furti.spring.web.extended.expression;

import io.github.furti.spring.web.extended.template.TemplateContext;

/**
 * @author Daniel Furtlehner
 */
public interface ExpressionHandler
{

    /**
     * @return the prefix of the expression handler. This prefix is used in templates to reference the expression
     *         handler to use.
     */
    String getPrefix();

    /**
     * @param value the expression value
     * @param templateContext the template context
     * @return the result produced by this expression handler for the context and expression value
     */
    String process(String value, TemplateContext templateContext);
}
