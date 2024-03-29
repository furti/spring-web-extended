/**
 * Copyright 2014 Daniel Furtlehner Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package io.github.furti.spring.web.extended.template.legacy.cache.script;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import io.github.furti.spring.web.extended.template.legacy.ResourceControllerBase;
import io.github.furti.spring.web.extended.util.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = { "/script", "/*/*/script", "/*/*/*/script" })
public class ScriptController extends ResourceControllerBase {

    private ScriptStacks stacks;

    @RequestMapping(value = "/single/{stackId}/{scriptName}", method = RequestMethod.GET, produces = "text/javascript; charset=UTF-8")
    @ResponseBody
    public String handleScript(@PathVariable("stackId") String stackId, @PathVariable("scriptName") String scriptName,
            HttpServletResponse response) {
        if (stacks == null || !stacks.hasStack(stackId)) {
            throw new ResourceNotFoundException(String.format("%s:%s", stackId, scriptName));
        }

        ScriptStack stack = stacks.get(stackId);

        if (!stack.hasTemplate(scriptName)) {
            throw new ResourceNotFoundException(String.format("%s:%s", stackId, scriptName));
        }

        handleCaching(response, stack.isNoCaching());

        return stack.renderTemplate(scriptName);
    }

    @RequestMapping(value = "/stack/{stackId}", method = RequestMethod.GET, produces = "text/javascript; charset=UTF-8")
    @ResponseBody
    public String handleStack(@PathVariable("stackId") String stackId, HttpServletResponse response) {
        if (stacks == null || !stacks.hasStack(stackId)) {
            throw new ResourceNotFoundException(String.format("%s:*", stackId));
        }

        ScriptStack stack = stacks.get(stackId);

        handleCaching(response, stack.isNoCaching());

        return stack.renderAll();
    }

    @Autowired
    public void setStacks(ScriptStacks stacks) {
        this.stacks = stacks;
    }
}
