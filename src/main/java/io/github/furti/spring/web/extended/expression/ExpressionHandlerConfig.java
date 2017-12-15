/**
 * 
 */
package io.github.furti.spring.web.extended.expression;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.furti.spring.web.extended.expression.handlers.MessageExpressionHandler;
import io.github.furti.spring.web.extended.staticfolder.StaticFolderConfigurerConfiguration;

/**
 * @author Daniel Furtlehner
 */
@Configuration
public class ExpressionHandlerConfig
{

    @Autowired
    private StaticFolderConfigurerConfiguration configurers;

    @Bean
    public ExpressionHandlerRegistry expressionHandlerRegistry(MessageSource messageSource)
    {
        ExpressionHandlerRegistry registry = new DefaultExpressionHandlerRegistry();

        registry.registerExpressionHandler(new MessageExpressionHandler(messageSource));

        configurers.configureExpressionHandlers(registry);

        return registry;
    }
}
