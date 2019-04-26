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

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.request.HstRequestContext;

import com.mitchellbosecke.pebble.attributes.AttributeResolver;
import com.mitchellbosecke.pebble.attributes.ResolvedAttribute;
import com.mitchellbosecke.pebble.node.ArgumentsNode;
import com.mitchellbosecke.pebble.template.EvaluationContextImpl;
import com.sun.xml.internal.ws.client.RequestContext;

public class PebbleHstAttributeResolver implements AttributeResolver {
    @Override
    public ResolvedAttribute resolve(final Object instance, final Object attributeNameValue, final Object[] argumentValues, final ArgumentsNode args, final EvaluationContextImpl context, final String filename, final int lineNumber) {
        if (instance instanceof HippoBean) {
            final HstRequestContext ctx = RequestContextProvider.get();
            // TODO
            return new ResolvedAttribute("test");
        }

        return null;
    }
}
