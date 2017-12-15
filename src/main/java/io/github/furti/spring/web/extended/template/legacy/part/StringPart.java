package io.github.furti.spring.web.extended.template.legacy.part;

/**
 * @author Daniel Furtlehner
 *
 */
public class StringPart implements TemplatePart
{
    private final String string;

    public StringPart(String string)
    {
        this.string = string;
    }

    @Override
    public String render()
    {
        return string;
    }

    @Override
    public String toString()
    {
        return string;
    }
}
