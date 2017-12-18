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

}
