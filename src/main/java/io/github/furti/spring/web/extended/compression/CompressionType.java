/**
 * 
 */
package io.github.furti.spring.web.extended.compression;

/**
 * @author Daniel Furtlehner
 */
public enum CompressionType
{
    NO_COMPRESSION("identity"),
    GZIP("gzip");

    private final String value;

    private CompressionType(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }

}
