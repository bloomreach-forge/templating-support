/*
 * Copyright 2018-2024 Bloomreach B.V. (http://www.bloomreach.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *         http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onehippo.forge.templating.support.handlebars.servlet;

import java.util.HashMap;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.map.LazyMap;

public class HandlebarsContextModelMap extends LazyMap {

    private static final long serialVersionUID = 1L;

    HandlebarsContextModelMap(Transformer factory) {
        super(new HashMap<String, Object>(), factory);
    }

}
