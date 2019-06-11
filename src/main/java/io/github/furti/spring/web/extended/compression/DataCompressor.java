/**
 * 
 */
package io.github.furti.spring.web.extended.compression;

import java.io.IOException;

/**
 * @author Daniel Furtlehner
 */
@FunctionalInterface
public interface DataCompressor
{

    /**
     * @param data the uncompressed data
     * @return the compressed data
     * @throws IOException when something goes wrong compressing the data
     */
    byte[] compress(byte[] data) throws IOException;

}
