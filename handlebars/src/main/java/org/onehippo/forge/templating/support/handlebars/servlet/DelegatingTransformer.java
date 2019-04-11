/*
 * Copyright 2018-2019 Bloomreach B.V. (http://www.bloomreach.com)
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

import org.apache.commons.collections.Transformer;

class DelegatingTransformer implements Transformer {

    private final Transformer[] transformers;
    private final int size;

    DelegatingTransformer(final Transformer ... transformers) {
        this.transformers = transformers;
        this.size = (transformers != null) ? transformers.length : 0;
    }

    @Override
    public Object transform(Object input) {
        for (int i = 0; i < size; i++) {
            Object value = transformers[i].transform(input);

            if (value != null) {
                return value;
            }
        }

        return null;
    }

}
