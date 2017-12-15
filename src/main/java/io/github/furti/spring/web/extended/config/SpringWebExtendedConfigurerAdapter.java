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
package io.github.furti.spring.web.extended.config;

import java.util.List;
import java.util.Map;

import io.github.furti.spring.web.extended.asset.CdnConfig;
import io.github.furti.spring.web.extended.expression.legacy.ExpressionHandler;
import io.github.furti.spring.web.extended.io.ResourceScanner;
import io.github.furti.spring.web.extended.locale.LocaleSource;
import io.github.furti.spring.web.extended.messagesource.MessageSourceConfig;
import io.github.furti.spring.web.extended.template.legacy.cache.StackConfig;
import io.github.furti.spring.web.extended.template.legacy.optimize.OptimizerConfig;

/**
 * @author Daniel Furtlehner
 *
 */
public abstract class SpringWebExtendedConfigurerAdapter implements SpringWebExtendedConfigurer
{

    @Override
    public void configureMessageSource(MessageSourceConfig config)
    {
        // Subclasses may override this to add configurations
    }

    @Override
    public void configureStyles(StackConfig config)
    {
        // Subclasses may override this to add configurations
    }

    @Override
    public void configureScripts(StackConfig config)
    {
        // Subclasses may override this to add configurations
    }

    @Override
    public void configureApplication(ApplicationConfiguration config)
    {
        // Subclasses may override this to add configurations
    }

    @Override
    public void configureOptimizers(OptimizerConfig config)
    {
        // Subclasses may override this to add configurations
    }

    @Override
    public void configureResourceScanners(Map<String, ResourceScanner> config)
    {
        // Subclasses may override this to add configurations
    }

    @Override
    public void configureExpressionHandlers(Map<String, ExpressionHandler> config)
    {
        // Subclasses may override this to add configurations
    }

    @Override
    public void configureHtmlTemplates(StackConfig htmlConfig)
    {
        // Subclasses may override this to add configurations
    }

    @Override
    public void configureLocaleSources(List<LocaleSource> sources)
    {
        // Subclasses may override this to add configurations
    }

    @Override
    public void configureCDN(CdnConfig config)
    {
        // Subclasses may override this to add own configurations
    }
}
