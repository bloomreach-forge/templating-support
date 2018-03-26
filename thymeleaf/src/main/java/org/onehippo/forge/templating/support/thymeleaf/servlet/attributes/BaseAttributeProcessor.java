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

package org.onehippo.forge.templating.support.thymeleaf.servlet.attributes;

import com.google.common.base.Strings;
import org.onehippo.forge.templating.support.thymeleaf.servlet.utils.ThymeleafHstUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IAttribute;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.model.ITemplateEvent;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.util.FastStringWriter;

import java.io.IOException;

public abstract class BaseAttributeProcessor extends AbstractAttributeTagProcessor {

    private static final Logger log = LoggerFactory.getLogger(BaseAttributeProcessor.class);
    private static final int PRECEDENCE = 10000;

    public static final String ATTRIBUTE_HST_PARAMS = "hst:params";
    public static final String ATTR_FULLY_QUALIFIED = "hst:fullyQualified";

    public BaseAttributeProcessor(final String dialectPrefix, final String attributeName) {
        super(TemplateMode.HTML, dialectPrefix, null, false, attributeName, true, PRECEDENCE, true);
    }


    protected boolean parseBoolean(final IAttribute attribute) {
        if (attribute == null) {
            return false;
        }
        final String value = attribute.getValue();
        return Boolean.parseBoolean(value);
    }

    protected void setLink(final IElementTagStructureHandler structureHandler, final IProcessableElementTag tag, final String link) {
        final String tagName = tag.getElementCompleteName();
        switch (tagName) {
            case "a":
            case "link":
                structureHandler.setAttribute("href", link);
                break;
            case "script":
                structureHandler.setAttribute("src", link);
                break;
            case "form":
                structureHandler.setAttribute("form", link);
                break;
            default:
                structureHandler.setAttribute("href", link);
        }
    }

    public static FastStringWriter modelAsString(final IModel model) {
        final FastStringWriter writer = new FastStringWriter(1024);
        final int modelSize = model.size();
        //writer.write(STRING_PROTOCOL);
        for (int i = 0; i < modelSize; i++) {
            final ITemplateEvent event = model.get(i);
            try {
                event.write(writer);
            } catch (IOException e) {
                log.warn("Error writing header contribution part", e);
            }
        }
        return writer;
    }

    protected String getStringOrExpression(final ITemplateContext context, final String attributeValue) {
        if (Strings.isNullOrEmpty(attributeValue)) {
            return attributeValue;
        }
        if (attributeValue.contains("${") || attributeValue.contains("#{")) {
            return ThymeleafHstUtils.getExpression(context, attributeValue);
        }
        return attributeValue;
    }
}
