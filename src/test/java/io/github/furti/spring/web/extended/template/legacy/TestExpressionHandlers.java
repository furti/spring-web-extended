package io.github.furti.spring.web.extended.template.legacy;

import io.github.furti.spring.web.extended.expression.legacy.BaseExpressionHandler;
import io.github.furti.spring.web.extended.expression.legacy.ExpressionHandler;
import io.github.furti.spring.web.extended.expression.legacy.ExpressionHandlers;
import java.util.HashMap;

public class TestExpressionHandlers extends ExpressionHandlers {

    public TestExpressionHandlers() {
        super(new HashMap<String, ExpressionHandler>());
        handlers.put("simple", new SimpleExpressionHandler());
        handlers.put("static", new StaticExpressionHandler());
    }

    private static class SimpleExpressionHandler extends BaseExpressionHandler {

        public SimpleExpressionHandler() {
            super(true);
        }

        @Override
        public String doProcess(String value) {
            return value;
        }
    }

    private static class StaticExpressionHandler extends BaseExpressionHandler {

        public StaticExpressionHandler() {
            super(false);
        }

        @Override
        public String doProcess(String value) {
            return "static text";
        }
    }
}
