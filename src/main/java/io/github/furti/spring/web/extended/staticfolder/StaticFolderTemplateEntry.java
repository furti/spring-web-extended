/**
 *
 */
package io.github.furti.spring.web.extended.staticfolder;

import io.github.furti.spring.web.extended.compression.CompressionManager;
import io.github.furti.spring.web.extended.compression.CompressionResponse;
import io.github.furti.spring.web.extended.compression.CompressionType;
import io.github.furti.spring.web.extended.template.Template;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Daniel Furtlehner
 */
public class StaticFolderTemplateEntry {

    private final ConcurrentHashMap<CompressionType, byte[]> compressionCache = new ConcurrentHashMap<>(1);
    private final Template template;
    private final CompressionManager compressionMananger;

    public StaticFolderTemplateEntry(Template template, CompressionManager compressionMananger) {
        super();
        this.template = template;
        this.compressionMananger = compressionMananger;
    }

    /**
     * @param force true to force refreshing even when nothing changed.
     * @throws IOException when something goes wrong while refreshing the template
     */
    public void refresh(boolean force) throws IOException {
        if (force) {
            template.forceRefresh();
            compressionCache.clear();
        } else {
            if (template.refreshIfNeeded()) {
                compressionCache.clear();
            }
        }
    }

    public long getLastRefreshed() {
        return template.getLastRefreshed();
    }

    public StaticFolderRenderResponse render(CompressionType requestedCompressionType, Charset charset)
        throws IOException {
        byte[] compressed = compressionCache.get(requestedCompressionType);
        CompressionType actualCompressionType = requestedCompressionType;

        if (compressed == null) {
            CompressionResponse compressionResponse = compressionMananger.compressData(
                template.render().getBytes(charset),
                requestedCompressionType
            );

            compressed = compressionResponse.getData();
            actualCompressionType = compressionResponse.getActualCompressionType();

            // No need to cache uncompressed data again
            if (actualCompressionType != CompressionType.NO_COMPRESSION) {
                compressionCache.put(actualCompressionType, CommonContentCache.getCommonContent(compressed));
            }
        }

        StaticFolderRenderResponse response = new StaticFolderRenderResponse(compressed);

        if (actualCompressionType != CompressionType.NO_COMPRESSION) {
            response.setContentEncoding(actualCompressionType.getValue());
        }

        return response;
    }
}
