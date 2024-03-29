/**
 * 
 */
package io.github.furti.spring.web.extended.staticfolder;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * If a request is not mapped by another controller we will fall back to our static folders. If we find a resource for
 * this request we serve it. Otherwise we throw a resource not found exception.
 * 
 * @author Daniel Furtlehner
 */
// This is not a controller on purpose. Should be picked up by our own HandlerMapping and not by Springs own handler mapping.
@Component
public class StaticFoldersController
{

    private final StaticFolderCache cache;

    @Autowired
    public StaticFoldersController(StaticFolderCache cache)
    {
        super();
        this.cache = cache;
    }

    @RequestMapping(value = "**", method = RequestMethod.GET)
    public ResponseEntity<byte[]> handleRequest(HttpServletRequest request)
    {
        return cache.render(request);
    }
}
