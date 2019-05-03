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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mitchellbosecke.pebble.extension.NodeVisitor;
import com.mitchellbosecke.pebble.node.RenderableNode;
import com.mitchellbosecke.pebble.node.expression.ContextVariableExpression;
import com.mitchellbosecke.pebble.node.expression.MapExpression;
import com.mitchellbosecke.pebble.template.EvaluationContextImpl;
import com.mitchellbosecke.pebble.template.PebbleTemplateImpl;

public class PebbleHstLinkNode implements RenderableNode {
    private static final Logger log = LoggerFactory.getLogger(PebbleHstLinkNode.class);
    private final int lineNumber;
    private final String path;
    private final boolean full;
    private final ContextVariableExpression expression;



    public PebbleHstLinkNode(final int lineNumber, final ContextVariableExpression expression, final boolean full) {
        this.lineNumber = lineNumber;
        this.expression = expression;
        this.path = null;
        this.full = full;
    }

    public PebbleHstLinkNode(final int lineNumber, final String value, final boolean full) {
        this.lineNumber = lineNumber;
        this.expression = null;
        this.path = value;
        this.full = full;

    }

    @Override
    public void render(final PebbleTemplateImpl self, final Writer writer, final EvaluationContextImpl context) throws IOException {
        String result = "";
        Object evaluate = null;
        if (expression != null) {
            evaluate = expression.evaluate(self, context);
        }
        if (evaluate instanceof HippoBean) {
            result = HstLinkHelper.INSTANCE.linkByHippoBean((HippoBean) evaluate, full);

        } else if (path != null) {
            result = HstLinkHelper.INSTANCE.linkByPath(path, full);
        }
        writer.write(result);
        //context.getScopeChain().set(this.name, this.value.evaluate(self, context));
    }

    @Override
    public void accept(final NodeVisitor visitor) {
        visitor.visit(this);
    }
}
