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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mitchellbosecke.pebble.attributes.AttributeResolver;
import com.mitchellbosecke.pebble.extension.AbstractExtension;
import com.mitchellbosecke.pebble.extension.Function;
import com.mitchellbosecke.pebble.tokenParser.TokenParser;

public class PebbleHstExtension extends AbstractExtension {


    @Override
    public List<AttributeResolver> getAttributeResolver() {
        final List<AttributeResolver> resolvers = new ArrayList<>();
        resolvers.add(new PebbleHstAttributeResolver());
        return resolvers;
    }

    @Override
    public List<TokenParser> getTokenParsers() {
        final List<TokenParser> parsers = new ArrayList<>();
        parsers.add(new PebbleHstLinkTag());
        parsers.add(new PebbleHstRenderURLTag());
        return parsers;
    }

    @Override
    public Map<String, Function> getFunctions() {
        final Map<String, Function> functions = new HashMap<>();
        functions.put(PebbleHstTestFunction.NAME, new PebbleHstTestFunction());
        return functions;
    }


}
