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

import org.onehippo.forge.templating.support.core.helper.HstHeadContributionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.*;
import org.thymeleaf.processor.element.AbstractElementModelProcessor;
import org.thymeleaf.processor.element.IElementModelStructureHandler;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.util.FastStringWriter;

import java.io.IOException;

public class ThymeleafHstHeadContributionTag extends AbstractElementModelProcessor {

    private static final Logger log = LoggerFactory.getLogger(ThymeleafHstHeadContributionTag.class);
    private static final String TAG_NAME = "headContribution";

    public ThymeleafHstHeadContributionTag(final String dialectPrefix) {
        super(TemplateMode.HTML, dialectPrefix, TAG_NAME, true, null, false, 1000);
    }


    @Override
    protected void doProcess(final ITemplateContext context, final IModel model, final IElementModelStructureHandler structureHandler) {
        final int size = model.size();
        String attrKeyHint = null;
        String attrCategory = null;
        for (int i = 0; i < size; i++) {
            final ITemplateEvent event = model.get(i);
            if (event instanceof IOpenElementTag) {
                final IOpenElementTag openElementTag = (IOpenElementTag) event;
                final IAttribute keyHint = openElementTag.getAttribute("keyHint");
                if (keyHint != null) {
                    attrKeyHint = keyHint.getValue();
                }
                final IAttribute category = openElementTag.getAttribute("category");
                if (category != null) {
                    attrCategory = category.getValue();
                }
                break;
            }
        }
        final IModel noTagsModel = removeTags(model);
        // remove original model:
        model.reset();
        final int modelSize = noTagsModel.size();
        final FastStringWriter writer = new FastStringWriter(1024);
        //writer.write(STRING_PROTOCOL);
        for (int i = 0; i < modelSize; i++) {
            final ITemplateEvent event = noTagsModel.get(i);
            try {
                event.write(writer);
            } catch (IOException e) {
                log.warn("Error writing header contribution part", e);
            }
        }
        // TODO parse expressions
        HstHeadContributionHelper.INSTANCE.contributeHeadElement(writer.toString(), attrKeyHint, attrCategory);

    }


    private String parseExpression(final IStandardExpressionParser parser, final ITemplateContext context, final String attributeValue) {
        final IStandardExpression expression = parser.parseExpression(context, attributeValue);
        return (String) expression.execute(context);
    }

    private IModel removeTags(IModel model) {
        final IModel clonedModel = model.cloneModel();
        final int size = clonedModel.size();
        int removed = 0;
        for (int i = 0; i < size; i++) {
            final ITemplateEvent event = model.get(i);
            if (event instanceof IOpenElementTag || event instanceof ICloseElementTag) {
                clonedModel.remove(i - removed);
                removed++;
            }
        }
        return clonedModel;
    }
}
