/**
 *
 */
package io.github.furti.spring.web.extended.expression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * @author Daniel Furtlehner
 */
public class DefaultExpressionHandlerRegistry implements ExpressionHandlerRegistry {

    private final List<ExpressionHandler> expressionHandlers = new ArrayList<>();

    @Override
    public ExpressionHandlerRegistry registerExpressionHandler(ExpressionHandler expressionHandler)
        throws IllegalArgumentException {
        assertUnique(expressionHandler);

        expressionHandlers.add(expressionHandler);

        return this;
    }

    @Override
    public ExpressionHandlerRegistry removeExpressionHandler(String prefix) {
        Iterator<ExpressionHandler> iterator = expressionHandlers.iterator();

        while (iterator.hasNext()) {
            ExpressionHandler handler = iterator.next();

            if (Objects.equals(handler.getPrefix(), prefix)) {
                iterator.remove();

                break;
            }
        }

        return this;
    }

    @Override
    public List<ExpressionHandler> getExpressionHandlers() {
        return Collections.unmodifiableList(expressionHandlers);
    }

    @Override
    public ExpressionHandler getExpressionHandler(String prefix) {
        for (ExpressionHandler expressionHandler : expressionHandlers) {
            if (Objects.equals(prefix, expressionHandler.getPrefix())) {
                return expressionHandler;
            }
        }

        return null;
    }

    private void assertUnique(ExpressionHandler expressionHandler) {
        for (ExpressionHandler registeredHandler : expressionHandlers) {
            if (Objects.equals(registeredHandler.getPrefix(), expressionHandler.getPrefix())) {
                throw new IllegalArgumentException(
                    String.format(
                        "A ExpressionHandler with prefix %s is already registered.",
                        expressionHandler.getPrefix()
                    )
                );
            }
        }
    }
}
