/**
 *
 */
package io.github.furti.spring.web.extended.compression;

/**
 * @author Daniel Furtlehner
 */
public class CompressionResponse {

    private final CompressionType actualCompressionType;
    private final byte[] data;

    public CompressionResponse(CompressionType actualCompressionType, byte[] data) {
        super();
        this.actualCompressionType = actualCompressionType;
        this.data = data;
    }

    public CompressionType getActualCompressionType() {
        return actualCompressionType;
    }

    public byte[] getData() {
        return data;
    }
}
