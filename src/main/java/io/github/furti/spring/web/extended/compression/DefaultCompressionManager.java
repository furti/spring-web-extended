/**
 * 
 */
package io.github.furti.spring.web.extended.compression;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.util.MimeType;

/**
 * @author Daniel Furtlehner
 */
public class DefaultCompressionManager implements CompressionManager
{
    private static final long MIN_CONTENT_LENGTH_FOR_COMPRESSION = 800;

    private static final Map<CompressionType, DataCompressor> COMPRESSORS =
        new HashMap<CompressionType, DataCompressor>();

    static
    {
        // No compression means simply use the original data
        COMPRESSORS.put(CompressionType.NO_COMPRESSION, (data) -> data);
        COMPRESSORS.put(CompressionType.GZIP, new GzipDataCompressor());
    }

    private final List<MimeType> supportedMimeTypes;

    public DefaultCompressionManager(List<MimeType> supportedMimeTypes)
    {
        super();

        this.supportedMimeTypes =
            supportedMimeTypes != null ? Collections.unmodifiableList(supportedMimeTypes) : Collections.emptyList();
    }

    @Override
    public CompressionType getRequestedCompressionType(HttpServletRequest request, MimeType responseContentType)
    {
        if (!isMimeTypeSupported(responseContentType))
        {
            return CompressionType.NO_COMPRESSION;
        }

        return requestedCompression(request);
    }

    @Override
    public CompressionResponse compressData(byte[] data, @Nonnull CompressionType requestedCompressionType) throws IOException
    {
        if (data.length < MIN_CONTENT_LENGTH_FOR_COMPRESSION)
        {
            return new CompressionResponse(CompressionType.NO_COMPRESSION, data);
        }

        if (!COMPRESSORS.containsKey(requestedCompressionType))
        {
            throw new IllegalArgumentException("CompressionType " + requestedCompressionType + " is not supported");
        }

        return new CompressionResponse(requestedCompressionType,
            COMPRESSORS.get(requestedCompressionType).compress(data));
    }

    private boolean isMimeTypeSupported(MimeType responseContentType)
    {
        for (MimeType mimeType : supportedMimeTypes)
        {
            if (mimeType.isCompatibleWith(responseContentType))
            {
                return true;
            }
        }

        return false;
    }

    private CompressionType requestedCompression(HttpServletRequest request)
    {
        String acceptEncoding = request.getHeader(HttpHeaders.ACCEPT_ENCODING);

        if (acceptEncoding == null)
        {
            return CompressionType.NO_COMPRESSION;
        }

        String[] requestedEncodings = acceptEncoding.split(",");

        boolean gzipSupported = false;

        for (String encoding : requestedEncodings)
        {
            if (encoding.trim().startsWith("gzip"))
            {
                gzipSupported = true;
            }
        }

        if (gzipSupported)
        {
            return CompressionType.GZIP;
        }

        return CompressionType.NO_COMPRESSION;
    }
}
