/**
 *
 */
package io.github.furti.spring.web.extended.template.simple;

import io.github.furti.spring.web.extended.expression.BaseExpressionHandler;
import io.github.furti.spring.web.extended.template.TemplateContext;

/**
 * @author Daniel Furtlehner
 */
public class ValueReturningExpressionHandler extends BaseExpressionHandler {

    public ValueReturningExpressionHandler() {
        super("value", false);
    }

    @Override
    protected String doProcess(String value, TemplateContext templateContext) {
        return value;
    }
}
