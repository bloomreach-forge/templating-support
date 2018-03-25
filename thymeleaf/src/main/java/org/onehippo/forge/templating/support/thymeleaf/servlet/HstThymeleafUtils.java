/*
 * Copyright 2018 Hippo B.V. (http://www.onehippo.com)
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

package org.onehippo.forge.templating.support.thymeleaf.servlet;

import com.google.common.base.Strings;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IAttribute;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;

public final class HstThymeleafUtils {
    private HstThymeleafUtils() {
    }

    @SuppressWarnings("unchecked")
    public static  <T> T getExpression(final ITemplateContext context, final String attributeValue) {
        if (Strings.isNullOrEmpty(attributeValue)) {
            return null;
        }
        final IEngineConfiguration configuration = context.getConfiguration();
        final IStandardExpressionParser parser = StandardExpressions.getExpressionParser(configuration);
        final IStandardExpression expression = parser.parseExpression(context, attributeValue);
        return (T) expression.execute(context);
    }

    public static  <T> T getExpression(final ITemplateContext context, final IProcessableElementTag tag, final String attributeName) {
        final String value = getAttribute(tag, attributeName);
        if (value == null) {
            return null;
        }
        return getExpression(context, value);
    }

    public static String getAttribute(final IProcessableElementTag tag, final String name) {
        final IAttribute attribute = tag.getAttribute(name);
        if (attribute == null) {
            return null;
        }
        final String value = attribute.getValue();
        if (value == null) {
            return null;
        }
        return value;
    }
}
