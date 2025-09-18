package io.github.furti.spring.web.extended.io;

import jakarta.annotation.Nullable;
import java.io.IOException;
import java.util.Map;
import org.springframework.core.io.Resource;

public interface ResourceScanner {
    /**
     * @param pattern the pattern to scan for
     * @param basePath to use for constructing relative paths. When null the base path will be constructed from the
     *            pattern.
     * @return Map<relative path, Resource>
     * @throws IOException
     */
    Map<String, Resource> scanResources(String pattern, @Nullable String basePath) throws IOException;

    Map<String, Resource> scanResources(String path, String file, boolean scanSubDirectories) throws IOException;
}
