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
package io.github.furti.spring.web.extended.template.legacy;

import io.github.furti.spring.web.extended.config.ApplicationConfiguration;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * @author Daniel Furtlehner
 *
 */
public abstract class ResourceControllerBase {

    private ApplicationConfiguration appConfig;

    protected void handleCaching(HttpServletResponse response, boolean noCaching) {
        if (appConfig.isOptimizeResources() && !noCaching) {
            response.setHeader("Cache-Control", "public, max-age=31536000");
            response.setHeader("Pragma", "cache");
        } else {
            response.setHeader("Cache-Control", "no-store, max-age=0, must-revalidate");
            response.setHeader("Pragma", "no-cache");
        }
    }

    @Autowired
    public void setAppConfig(ApplicationConfiguration appConfig) {
        Assert.notNull(appConfig, "appConfig must not be null");

        this.appConfig = appConfig;
    }
}
