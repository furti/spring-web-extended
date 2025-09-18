/**
 * Copyright 2014 Daniel Furtlehner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.furti.spring.web.extended.locale;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Extracts the portion of a path at the specified partIndex. Each part must be separated by a /. A starting / will be
 * ignored.
 *
 * @author Daniel Furtlehner
 *
 */
public abstract class NthPathPartLocaleSource extends AbstractLocaleSource {

    private final int partIndex;

    protected NthPathPartLocaleSource(int partIndex) {
        this.partIndex = partIndex;
    }

    @Override
    protected String getPossibleLocale(HttpServletRequest request, HttpServletResponse response) {
        String path = getPath(request, response);

        if (path == null) {
            return null;
        }

        return extractPart(path);
    }

    protected String extractPart(String path) {
        int index = path.startsWith("/") ? partIndex + 1 : partIndex;
        String[] split = path.split("/");

        return split.length > index ? split[index] : null;
    }

    protected abstract String getPath(HttpServletRequest request, HttpServletResponse response);
}
