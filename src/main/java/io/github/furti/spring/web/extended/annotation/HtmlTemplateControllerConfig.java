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
package io.github.furti.spring.web.extended.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.ApplicationContext;

import io.github.furti.spring.web.extended.template.legacy.cache.html.HtmlTemplateController;

/**
 * Annotation used to configure the default template controller
 *
 * @author Daniel Furtlehner
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface HtmlTemplateControllerConfig
{
    /**
     * If set to true the default {@link HtmlTemplateController} is added to the {@link ApplicationContext}
     * 
     * @return true if the default {@link HtmlTemplateController} should be registered, false otherwise
     */
    boolean registerHtmlTemplateController() default true;

    /**
     * @return true if a fallback to the index template should be made if the requested Template is not found
     */
    boolean fallbackToIndex() default true;
}
