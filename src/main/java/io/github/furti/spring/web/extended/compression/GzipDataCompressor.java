/**
 * 
 */
package io.github.furti.spring.web.extended.compression;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

/**
 * @author Daniel Furtlehner
 */
public class GzipDataCompressor implements DataCompressor
{

    @Override
    public byte[] compress(byte[] data) throws IOException
    {
        ByteArrayOutputStream compressedData = new ByteArrayOutputStream();

        try (GZIPOutputStream out = new GZIPOutputStream(compressedData))
        {
            out.write(data);
        }

        return compressedData.toByteArray();
    }

}
