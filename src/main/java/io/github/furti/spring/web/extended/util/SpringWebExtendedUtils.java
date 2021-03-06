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

public final class SpringWebExtendedUtils
{

    private SpringWebExtendedUtils()
    {

    }

    public static String[] parseExpression(String expression)
    {
        if (expression == null)
        {
            return new String[]{};
        }

        int index = expression.indexOf(":");

        if (index == -1)
        {
            return new String[]{"", expression};
        }

        String[] split = new String[2];

        split[0] = expression.substring(0, index);
        split[1] = expression.substring(index + 1);

        return split;
    }

    public static String stripPrefix(String expression)
    {
        return parseExpression(expression)[1];
    }
}
