/**
 * 
 */
package io.github.furti.spring.web.extended.staticfolder;

/**
 * @author Daniel Furtlehner
 */
public class ResourceRenderException extends Exception
{
    private static final long serialVersionUID = 1L;

    public ResourceRenderException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ResourceRenderException(Throwable cause)
    {
        super(cause);
    }

    public ResourceRenderException(String message)
    {
        super(message);
    }

}
