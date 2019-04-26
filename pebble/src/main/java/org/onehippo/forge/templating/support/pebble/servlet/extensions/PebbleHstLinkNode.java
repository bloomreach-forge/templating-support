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

import java.io.IOException;
import java.io.Writer;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.onehippo.forge.templating.support.core.helper.HstLinkHelper;

import com.mitchellbosecke.pebble.extension.NodeVisitor;
import com.mitchellbosecke.pebble.node.RenderableNode;
import com.mitchellbosecke.pebble.node.expression.Expression;
import com.mitchellbosecke.pebble.template.EvaluationContextImpl;
import com.mitchellbosecke.pebble.template.PebbleTemplateImpl;

public class PebbleHstLinkNode implements RenderableNode {
    private final int lineNumber;
    private final String name;
    private final Expression<?> value;

    public PebbleHstLinkNode(final int lineNumber, final String name, final Expression<?> value) {
        this.lineNumber = lineNumber;
        this.name = name;
        this.value = value;
    }

    @Override
    public void render(final PebbleTemplateImpl self, final Writer writer, final EvaluationContextImpl context) throws IOException {
        final String result;
        final Object evaluate = this.value.evaluate(self, context);
        if (evaluate instanceof HippoBean) {
            result = HstLinkHelper.INSTANCE.linkByHippoBean((HippoBean) evaluate, false);

        }else{
            result = (String) evaluate;
        }
        writer.write(result);
        //context.getScopeChain().set(this.name, this.value.evaluate(self, context));
    }

    @Override
    public void accept(final NodeVisitor visitor) {
        visitor.visit(this);
    }
}
