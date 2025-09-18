/**
 *
 */
package io.github.furti.spring.web.extended.compression;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.springframework.util.MimeType;

/**
 * @author Daniel Furtlehner
 */
public interface CompressionManager {
    /**
     * Tries to find the compression type that matches the request. When multiple compression types match, the
     * implementation is free to choose the type that it thinks fits best. When not compression type matches, the
     * implementation should return {@link CompressionType#NO_COMPRESSION}.
     *
     * @param request the request
     * @param responseConentType the content type of the data to compress later on
     * @return the compression type to use for the given request
     */
    @Nonnull
    CompressionType getRequestedCompressionType(HttpServletRequest request, MimeType responseConentType);

    /**
     * Compresses the data with the given algorithm. The method is free to honor the requested compression type or
     * choose a different one. E.g. the implementation can decide to not compress small data due to the mimimal benefit
     * and added overhead.
     *
     * @param data the data to compress
     * @param requestedCompressionType the algorithm to apply
     * @return the compressed data containing the actual compression type used
     * @throws IOException when something goes wrong compressing the data
     */
    @Nonnull
    CompressionResponse compressData(@Nonnull byte[] data, @Nonnull CompressionType requestedCompressionType)
        throws IOException;
}
