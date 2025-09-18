/**
 *
 */
package io.github.furti.spring.web.extended.staticfolder;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Daniel Furtlehner
 */
public final class CommonContentCache {

    private static final Map<String, WeakReference<String>> CACHE = new WeakHashMap<>();
    private static final Map<byte[], WeakReference<byte[]>> BYTE_CACHE = new WeakHashMap<>();

    private CommonContentCache() {
        super();
    }

    public static synchronized String getCommonContent(String content) {
        if (!CACHE.containsKey(content)) {
            CACHE.put(content, new WeakReference<>(content));
        }

        return CACHE.get(content).get();
    }

    public static synchronized byte[] getCommonContent(byte[] content) {
        if (!BYTE_CACHE.containsKey(content)) {
            BYTE_CACHE.put(content, new WeakReference<>(content));
        }

        return BYTE_CACHE.get(content).get();
    }
}
