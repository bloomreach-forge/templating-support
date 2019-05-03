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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mitchellbosecke.pebble.lexer.Token;
import com.mitchellbosecke.pebble.lexer.TokenStream;
import com.mitchellbosecke.pebble.node.RenderableNode;
import com.mitchellbosecke.pebble.node.expression.ContextVariableExpression;
import com.mitchellbosecke.pebble.node.expression.Expression;
import com.mitchellbosecke.pebble.node.expression.LiteralStringExpression;
import com.mitchellbosecke.pebble.node.expression.MapExpression;
import com.mitchellbosecke.pebble.parser.Parser;
import com.mitchellbosecke.pebble.tokenParser.TokenParser;

public class PebbleHstLinkTag implements TokenParser {

    private static final Logger log = LoggerFactory.getLogger(PebbleHstLinkTag.class);

    @Override
    public String getTag() {
        return "hstLink";
    }



    public boolean isFull() {
        return false;
    }

    @Override
    public RenderableNode parse(final Token token, final Parser parser) {
        final TokenStream stream = parser.getStream();
        final int lineNumber = token.getLineNumber();
        // skip the "hstLink" token
        stream.next();
        final Expression<?> parsedExpression = parser.getExpressionParser().parseExpression();
        stream.expect(Token.Type.EXECUTE_END);

        if (parsedExpression instanceof ContextVariableExpression) {
            final ContextVariableExpression expression = (ContextVariableExpression) parsedExpression;

            return new PebbleHstLinkNode(lineNumber, expression, isFull());
        } else if (parsedExpression instanceof LiteralStringExpression) {
            final LiteralStringExpression literalStringExpression = (LiteralStringExpression) parsedExpression;
            return new PebbleHstLinkNode(lineNumber, literalStringExpression.getValue(), isFull());
        } else {
            final String format = String.format("Unexpected expression '%1s'.", parsedExpression
                    .getClass().getCanonicalName());
            log.warn("{},{},{}", format, token.getLineNumber(), stream.getFilename());
            return new PebbleHstLinkNode(lineNumber, "", isFull());

        }

    }
}
