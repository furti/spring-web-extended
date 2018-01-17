/**
 * 
 */
package io.github.furti.spring.web.extended.staticfolder;

/**
 * @author Daniel Furtlehner
 */
public enum ResourceType
{

    /**
     * Resource of this type will be processed by the configured template engine.
     */
    TEMPLATE,

    /**
     * Resources of this type will be served byte by byte as they are.
     */
    BINARY;
}
