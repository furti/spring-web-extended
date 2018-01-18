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

/**
 * @author Daniel Furtlehner
 *
 */
public final class HtmlUtils
{

    private HtmlUtils()
    {

    }

    public static String buildScriptLink(String url)
    {
        return "<script src=\"" + (url != null ? url : "") + "\" " + "type=\"text/javascript\"></script>";
    }

    public static String buildStyleLink(String url)
    {
        return "<link href=\"" + (url != null ? url : "") + "\" " + "type=\"text/css\" rel=\"stylesheet\">";
    }
}