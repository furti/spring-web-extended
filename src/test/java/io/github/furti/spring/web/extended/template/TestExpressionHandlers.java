package io.github.furti.spring.web.extended.template;

import java.util.HashMap;

import io.github.furti.spring.web.extended.expression.BaseExpressionHandler;
import io.github.furti.spring.web.extended.expression.ExpressionHandler;
import io.github.furti.spring.web.extended.expression.ExpressionHandlers;

public class TestExpressionHandlers extends ExpressionHandlers
{

    public TestExpressionHandlers()
    {
        super(new HashMap<String, ExpressionHandler>());
        handlers.put("simple", new SimpleExpressionHandler());
        handlers.put("static", new StaticExpressionHandler());
    }

    private static class SimpleExpressionHandler extends BaseExpressionHandler
    {
        public SimpleExpressionHandler()
        {
            super(true);
        }

        @Override
        public String doProcess(String value)
        {
            return value;
        }

    }

    private static class StaticExpressionHandler extends BaseExpressionHandler
    {

        public StaticExpressionHandler()
        {
            super(false);
        }

        @Override
        public String doProcess(String value)
        {
            return "static text";
        }

    }
}
