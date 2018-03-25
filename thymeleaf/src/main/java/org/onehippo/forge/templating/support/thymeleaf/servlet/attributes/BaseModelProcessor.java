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

import org.onehippo.forge.templating.support.thymeleaf.servlet.HstThymeleafUtils;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.*;
import org.thymeleaf.processor.element.AbstractElementModelProcessor;
import org.thymeleaf.templatemode.TemplateMode;

public abstract class BaseModelProcessor extends AbstractElementModelProcessor {


    private static final EmptyWrapperTag EMPTY_WRAPPER = new EmptyWrapperTag();
    protected static final int BUFFER_SIZE = 1024;

    public BaseModelProcessor(final String dialectPrefix, final String tagName) {
        super(TemplateMode.HTML, dialectPrefix, tagName, true, null, false, 1000);
    }


    protected <T> T getAttributeExpression(final ITemplateContext context, final IModel model, final String name) {
        final String value = getAttribute(model, name);
        if (value == null) {
            return null;
        }
        return HstThymeleafUtils.getExpression(context, value);
    }
    protected String getAttribute(final IModel model, final String name) {
        final int size = model.size();
        for (int i = 0; i < size; i++) {
            final ITemplateEvent event = model.get(i);
            if (event instanceof IOpenElementTag) {
                final IOpenElementTag tag = (IOpenElementTag) event;
                final IAttribute attribute = tag.getAttribute(name);
                if (attribute != null) {
                    return attribute.getValue();
                }
            } else if (event instanceof IStandaloneElementTag) {
                final IStandaloneElementTag tag = (IStandaloneElementTag) event;
                final IAttribute attribute = tag.getAttribute(name);
                if (attribute != null) {
                    return attribute.getValue();
                }
            }
        }
        return null;
    }

    protected IModel wrapWithEmpty(IModel model) {
        final IModel clonedModel = model.cloneModel();
        final int size = clonedModel.size();
        for (int i = 0; i < size; i++) {
            final ITemplateEvent event = model.get(i);
            if (event instanceof IOpenElementTag) {
                clonedModel.replace(i, EMPTY_WRAPPER);
            }
        }
        clonedModel.replace(model.size() - 1, EMPTY_WRAPPER);
        return clonedModel;
    }

    protected IModel removeEnclosingTags(IModel model) {
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
