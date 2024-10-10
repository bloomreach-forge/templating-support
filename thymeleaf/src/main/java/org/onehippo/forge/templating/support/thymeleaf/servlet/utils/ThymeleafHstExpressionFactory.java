/*
 * Copyright 2018-2024 Bloomreach B.V. (http://www.bloomreach.com)
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

package org.onehippo.forge.templating.support.thymeleaf.servlet.utils;

import com.google.common.collect.ImmutableSet;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.expression.IExpressionObjectFactory;

import java.util.Set;

public class ThymeleafHstExpressionFactory implements IExpressionObjectFactory {
    
    private static final String MESSAGE_REPLACE = "messagesReplace";
    private static final String HST_FUNCTIONS = "hstFunctions";
    
    private static final ImmutableSet<String> NAMES = ImmutableSet.of(MESSAGE_REPLACE, HST_FUNCTIONS);

    @Override public Set<String> getAllExpressionObjectNames() {
        return NAMES;
    }

    @Override public Object buildObject(final IExpressionContext context, final String expressionObjectName) {
        if (HST_FUNCTIONS.equals(expressionObjectName)) {
            return new ThymeleafHstFunctionsExpression();
        }if (MESSAGE_REPLACE.equals(expressionObjectName)) {
            return new ThymeleafHstMessageReplaceExpression();
        }
        return null;
    }

    @Override public boolean isCacheable(final String expressionObjectName) {
        return expressionObjectName != null && NAMES.contains(expressionObjectName);
    }
}
