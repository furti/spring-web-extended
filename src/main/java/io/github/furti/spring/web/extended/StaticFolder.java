/**
 *
 */
package io.github.furti.spring.web.extended;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Daniel Furtlehner
 */
public class StaticFolder {

    private final String basePath;
    private final String location;
    private final Charset charset;
    private final Set<String> indexFallbacks;

    public StaticFolder(String basePath, String location, Charset charset, String[] indexFallbacks) {
        super();
        this.basePath = Objects.requireNonNull(basePath, "basePath must not be null");
        this.location = Objects.requireNonNull(location, "location must not be null");
        this.charset = Objects.requireNonNull(charset, "charset must not be null");
        this.indexFallbacks = this.processIndexFallbacks(indexFallbacks);

        if (!this.location.endsWith("/")) {
            throw new IllegalArgumentException("The location must end with /");
        }

        if (!basePath.startsWith("/")) {
            throw new IllegalArgumentException("The basePath must start with /");
        }
    }

    public String getBasePath() {
        return basePath;
    }

    public String getLocation() {
        return location;
    }

    public Charset getCharset() {
        return charset;
    }

    public Set<String> getIndexFallbacks() {
        return indexFallbacks;
    }

    private Set<String> processIndexFallbacks(String[] indexFallbacks) {
        Set<String> fallbacks = new HashSet<>();

        for (String fallback : indexFallbacks) {
            if (!fallback.startsWith("/")) {
                throw new IllegalArgumentException(
                    String.format("The fallback %s for location %s must start with a /", fallback, this.location)
                );
            }

            fallbacks.add(fallback);
        }

        return Collections.unmodifiableSet(fallbacks);
    }

    @Override
    public String toString() {
        return (
            "StaticFolder [basePath=" +
            basePath +
            ", location=" +
            location +
            ", charset=" +
            charset +
            ", indexFallbacks=" +
            indexFallbacks +
            "]"
        );
    }
}
