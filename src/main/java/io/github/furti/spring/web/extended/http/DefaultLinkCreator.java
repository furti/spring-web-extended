/**
 * Copyright 2014 Daniel Furtlehner Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package io.github.furti.spring.web.extended.http;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

import io.github.furti.spring.web.extended.config.ApplicationConfiguration;
import io.github.furti.spring.web.extended.servlet.RequestResponseContextHolder;

public class DefaultLinkCreator implements LinkCreator
{
    private ApplicationConfiguration appConfig;

    @Override
    public String createLink(String... parts)
    {
        if (parts == null)
        {
            return null;
        }

        StringBuilder url = new StringBuilder();

        prefix(url);

        if (url.length() == 0 || url.charAt(url.length() - 1) != '/')
        {
            url.append("/");
        }

        url.append(LocaleContextHolder.getLocale().toLanguageTag());

        if (appConfig != null && appConfig.getVersion() != null)
        {
            url.append("/").append(appConfig.getVersion());
        }

        for (String part : parts)
        {
            if (!part.startsWith("/"))
            {
                url.append("/");
            }

            url.append(part);
        }

        suffix(url);

        return url.toString();
    }

    /**
     * Subclasses may add something to the url before the default url parts get added
     *
     * @param url the URL
     */
    protected void prefix(StringBuilder url)
    {
        HttpServletRequest request = RequestResponseContextHolder.currentRequest();

        if (request != null && request.getContextPath() != null)
        {
            url.append(request.getContextPath());
        }
    };

    /**
     * Subclasses may add something to the url after the default url parts were added.
     *
     * @param url the URL
     */
    protected void suffix(StringBuilder url)
    {
        // Nothing to do here
    };

    @Autowired
    public void setAppConfig(ApplicationConfiguration appConfig)
    {
        this.appConfig = appConfig;
    }
}
