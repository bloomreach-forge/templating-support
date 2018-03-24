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
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.TemplateModel;
import org.thymeleaf.model.*;
import org.thymeleaf.processor.element.AbstractElementModelProcessor;
import org.thymeleaf.processor.element.IElementModelStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.util.FastStringWriter;

import java.io.IOException;
import java.io.Writer;

public class ThymeleafHstHeadContributionTag extends AbstractElementModelProcessor {

    private static final Logger log = LoggerFactory.getLogger(ThymeleafHstHeadContributionTag.class);
    private static final String TAG_NAME = "headContribution";
    private static final EmptyWrapperTag EMPTY_WRAPPER = new EmptyWrapperTag();
    private static final int BUFFER_SIZE = 1024;

    public ThymeleafHstHeadContributionTag(final String dialectPrefix) {
        super(TemplateMode.HTML, dialectPrefix, TAG_NAME, true, null, false, 1000);
    }


    @Override
    protected void doProcess(final ITemplateContext context, final IModel originalModel, final IElementModelStructureHandler structureHandler) {
        String attrKeyHint = getAttribute(originalModel, "hst:keyHint");
        String attrCategory = getAttribute(originalModel, "hst:category");
        final IModel model = wrap(originalModel);
        // remove original model:
        originalModel.reset();
        final IEngineConfiguration configuration = context.getConfiguration();
        IProcessableElementTag firstEvent = (IProcessableElementTag) model.get(0);
        final Writer modelWriter = new FastStringWriter(BUFFER_SIZE);
        try {
            model.write(modelWriter);
            final TemplateModel temporaryModel = configuration.getTemplateManager()
                    .parseString(context.getTemplateData(), modelWriter.toString(), firstEvent.getLine(),
                            firstEvent.getCol(),
                            getTemplateMode(), true);
            final Writer templateWriter = new FastStringWriter(BUFFER_SIZE);
            configuration.getTemplateManager().process(temporaryModel, context, templateWriter);
            HstHeadContributionHelper.INSTANCE.contributeHeadElement(templateWriter.toString(), attrKeyHint, attrCategory);
        } catch (IOException e) {
            log.error("Error parsing head contribution {}", e);
        }

    }
    

    private String getAttribute(final IModel model, final String name) {
        final int size = model.size();
        for (int i = 0; i < size; i++) {
            final ITemplateEvent event = model.get(i);
            if (event instanceof IOpenElementTag) {
                final IOpenElementTag openElementTag = (IOpenElementTag) event;
                final IAttribute attribute = openElementTag.getAttribute(name);
                if (attribute != null) {
                    return attribute.getValue();
                }

            }
        }
        return null;
    }
    private IModel wrap(IModel model) {
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
