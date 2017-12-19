/**
 * 
 */
package io.github.furti.spring.web.extended.template.simple;

import io.github.furti.spring.web.extended.template.TemplateContext;

/**
 * @author Daniel Furtlehner
 */
public class StaticTemplatePart implements TemplatePart
{

    private final String content;

    public StaticTemplatePart(String content)
    {
        super();
        this.content = content;
    }

    @Override
    public String render(TemplateContext context)
    {
        return content;
    }

}
