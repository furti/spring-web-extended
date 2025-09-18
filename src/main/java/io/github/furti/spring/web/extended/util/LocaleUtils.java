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
package io.github.furti.spring.web.extended.util;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * @author Daniel Furtlehner
 */
public final class LocaleUtils {

    private LocaleUtils() {}

    public static Locale closestSupportedLocale(List<Locale> supportedLocales, String languageTag) {
        Locale locale = languageTag != null ? Locale.forLanguageTag(languageTag) : null;

        return closestSupportedLocale(supportedLocales, locale);
    }

    public static Locale closestSupportedLocale(List<Locale> supportedLocales, Locale locale) {
        if (locale == null || supportedLocales == null) {
            return null;
        }

        Locale closest = null;
        int closestMatches = 0;

        for (Locale l : supportedLocales) {
            int matches = 0;

            // If the language does not match no more processing is needed
            if (Objects.equals(l.getLanguage(), locale.getLanguage())) {
                matches++;

                // if the country does not match no more processing is needed
                if (Objects.equals(l.getCountry(), locale.getCountry())) {
                    matches++;

                    if (Objects.equals(l.getVariant(), locale.getVariant())) {
                        matches++;
                    }
                }
            }

            /*
             * If language, country and variant match, we have our locale and no
             * more iteration is needed
             */
            if (matches == 3) {
                return l;
            }

            if (matches > closestMatches) {
                closestMatches = matches;
                closest = l;
            }
        }

        return closest;
    }
}
