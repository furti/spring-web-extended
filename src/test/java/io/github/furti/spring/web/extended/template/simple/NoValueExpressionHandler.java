/**
 *
 */
package io.github.furti.spring.web.extended.template.simple;

import io.github.furti.spring.web.extended.expression.BaseExpressionHandler;
import io.github.furti.spring.web.extended.template.TemplateContext;

/**
 * @author Daniel Furtlehner
 */
public class NoValueExpressionHandler extends BaseExpressionHandler {

    public NoValueExpressionHandler() {
        super("novalue", false);
    }

    @Override
    protected String doProcess(String value, TemplateContext templateContext) {
        return "somevalue";
    }
}
