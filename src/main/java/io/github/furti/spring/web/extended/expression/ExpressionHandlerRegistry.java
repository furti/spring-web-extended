/**
 * 
 */
package io.github.furti.spring.web.extended.expression;

import java.util.List;

/**
 * @author Daniel Furtlehner
 */
public interface ExpressionHandlerRegistry
{

    /**
     * Registers an ExpressinonHandler.
     * 
     * @param expressionHandler the expression handler to register.
     * @throws IllegalArgumentException when a expression handler for the same prefix is already registered
     * @return the registry for a fluent api
     */
    ExpressionHandlerRegistry registerExpressionHandler(ExpressionHandler expressionHandler)
        throws IllegalArgumentException;

    /**
     * @param prefix the prefix of the expression handler to remove.
     * @return the registry for a fluent api
     */
    ExpressionHandlerRegistry removeExpressionHandler(String prefix);

    /**
     * @return unmodifiable list of expression handlers registered.
     */
    List<ExpressionHandler> getExpressionHandlers();

}
