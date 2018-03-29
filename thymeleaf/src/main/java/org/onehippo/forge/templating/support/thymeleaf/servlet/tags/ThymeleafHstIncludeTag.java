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

package org.onehippo.forge.templating.support.thymeleaf.servlet.tags;

import org.hippoecm.hst.core.component.HstResponse;
import org.onehippo.forge.templating.support.core.servlet.TemplateRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IModel;
import org.thymeleaf.processor.element.IElementModelStructureHandler;

import java.io.IOException;

public class ThymeleafHstIncludeTag extends BaseModelProcessor {
    private static final String TAG_NAME = "include";

    private static final Logger log = LoggerFactory.getLogger(ThymeleafHstIncludeTag.class);

    public ThymeleafHstIncludeTag(final String dialectPrefix) {
        super(dialectPrefix, TAG_NAME);
    }

    @Override
    protected void doProcess(final ITemplateContext context, final IModel model, final IElementModelStructureHandler structureHandler) {
        final String ref = getAttribute(model, "hst:ref");
        model.reset();
        final HstResponse hstResponse = TemplateRequestContext.getHstResponse();
        if (hstResponse == null) {
            return;
        }
        try {
            // TODO check HST logic
            hstResponse.flushChildContent(ref);
        } catch (IOException e) {
            log.error("Error rendering hst:include tag", e);
        }

    }

}
