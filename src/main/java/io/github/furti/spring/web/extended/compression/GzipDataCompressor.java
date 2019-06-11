/**
 * 
 */
package io.github.furti.spring.web.extended.compression;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;

/**
 * @author Daniel Furtlehner
 */
public class GzipDataCompressor implements DataCompressor
{

    @Override
    public byte[] compress(byte[] data) throws IOException
    {
        ByteArrayOutputStream compressedData = new ByteArrayOutputStream();

        try (GzipCompressorOutputStream out = new GzipCompressorOutputStream(compressedData))
        {
            out.write(data);
        }

        return compressedData.toByteArray();
    }

}
