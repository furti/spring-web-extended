/**
 *
 */
package io.github.furti.spring.web.extended.template;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Furtlehner
 */
public class DefaultTemplateContext implements TemplateContext
{
    private final Map<Object, Object> parameters = new HashMap<>();

    public DefaultTemplateContext addParameter(Object key, Object value)
    {
        parameters.put(key, value);

        return this;
    }

    @Override
    public Object getParameter(Object key)
    {
        return parameters.get(key);
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;

        result = (prime * result) + ((parameters == null) ? 0 : parameters.hashCode());

        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }

        if (obj == null)
        {
            return false;
        }

        if (getClass() != obj.getClass())
        {
            return false;
        }

        DefaultTemplateContext other = (DefaultTemplateContext) obj;

        if (parameters == null)
        {
            if (other.parameters != null)
            {
                return false;
            }
        }
        else if (!parameters.equals(other.parameters))
        {
            return false;
        }

        return true;
    }

}
