/**
 *
 */
package io.github.furti.spring.web.extended.staticfolder;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;

/**
 * @author Daniel Furtlehner
 */
public class StaticFolderRenderResponse {

    private final Map<String, String> headers = new HashMap<>();
    private final byte[] content;

    public StaticFolderRenderResponse(byte[] content) {
        super();
        this.content = content;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContentEncoding(String encoding) {
        headers.put(HttpHeaders.CONTENT_ENCODING, encoding);
    }
}
