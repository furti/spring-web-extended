/**
 *
 */
package io.github.furti.spring.web.extended.expression;

import io.github.furti.spring.web.extended.template.TemplateContext;

/**
 * @author Daniel Furtlehner
 */
public interface ExpressionHandler {
    /**
     * @return the prefix of the expression handler. This prefix is used in templates to reference the expression
     *         handler to use.
     */
    String getPrefix();

    /**
     * @param value the expression value
     * @param templateContext the template context
     * @return the result produced by this expression handler for the context and expression value. The handler should
     *         return null when he could not produce a result for the given value. The template engine might take an
     *         action in that case.
     */
    String process(String value, TemplateContext templateContext);

    /**
     * @return true when the expression handler needs a value. The template engine can perform some optimizations based
     *         on this setting.
     */
    boolean isValueRequired();
}
