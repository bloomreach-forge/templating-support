/*
 * Copyright 2019 Hippo B.V. (http://www.onehippo.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.onehippo.forge.templating.support.pebble.servlet.extensions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mitchellbosecke.pebble.extension.Function;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

public class PebbleHstTestFunction implements Function {


    public static final String NAME = "test";
    public static final String PARAM_PATH = "path";
    private final List<String> argumentNames;

    public PebbleHstTestFunction() {
        argumentNames = new ArrayList<>();
        argumentNames.add(PARAM_PATH);
    }

    @Override
    public Object execute(final Map<String, Object> args, final PebbleTemplate self, final EvaluationContext context, final int lineNumber) {
        final StringBuilder result = new StringBuilder();
        result.append("test");
        return result.toString();
    }


    @Override
    public List<String> getArgumentNames() {
        return argumentNames;
    }
}
