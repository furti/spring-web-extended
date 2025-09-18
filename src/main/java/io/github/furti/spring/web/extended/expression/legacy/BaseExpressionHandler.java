package io.github.furti.spring.web.extended.expression.legacy;

import io.github.furti.spring.web.extended.template.legacy.TemplateRenderContext;
import io.github.furti.spring.web.extended.template.legacy.TemplateRenderContextHolder;
import org.owasp.encoder.Encode;

public abstract class BaseExpressionHandler implements ExpressionHandler {

    private final boolean valueNeeded;

    public BaseExpressionHandler(boolean valueNeeded) {
        super();
        this.valueNeeded = valueNeeded;
    }

    @Override
    public String process(String value) {
        return escape(doProcess(value));
    }

    protected abstract String doProcess(String value);

    protected TemplateRenderContext getTemplateRenderContext() {
        return TemplateRenderContextHolder.actualContext();
    }

    private String escape(String value) {
        TemplateRenderContext context = getTemplateRenderContext();

        if (context == null || context.getResourceType() == null) {
            return value;
        }

        switch (context.getResourceType()) {
            case HTML:
                return Encode.forHtml(value);
            case SCRIPT:
                return Encode.forJavaScript(value);
            default:
                return value;
        }
    }

    @Override
    public boolean valueNeeded() {
        return valueNeeded;
    }
}
