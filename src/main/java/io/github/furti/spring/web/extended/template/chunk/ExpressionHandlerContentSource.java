/**
 * 
 */
package io.github.furti.spring.web.extended.template.chunk;

import com.x5.template.ContentSource;
import com.x5.template.Snippet;

import io.github.furti.spring.web.extended.expression.ExpressionHandler;
import io.github.furti.spring.web.extended.template.TemplateContext;

/**
 * @author Daniel Furtlehner
 */
public class ExpressionHandlerContentSource implements ContentSource
{
    private final TemplateContext templateContext;
    private final ExpressionHandler expressionHandler;

    public ExpressionHandlerContentSource(TemplateContext templateContext, ExpressionHandler expressionHandler)
    {
        super();
        this.templateContext = templateContext;
        this.expressionHandler = expressionHandler;
    }

    @Override
    public String fetch(String itemName)
    {
        return expressionHandler.process(itemName, templateContext);
    }

    @Override
    public boolean provides(String itemName)
    {
        return true;
    }

    @Override
    public String getProtocol()
    {
        return expressionHandler.getPrefix();
    }

    @Override
    public Snippet getSnippet(String snippetName)
    {
        return null;
    }

}
