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
import org.thymeleaf.model.IModel;
import org.thymeleaf.processor.element.IElementModelStructureHandler;

public class ThymeleafHstHeadContributionsTag extends BaseModelProcessor {

    private static final Logger log = LoggerFactory.getLogger(ThymeleafHstHeadContributionsTag.class);
    private static final String TAG_NAME = "headContributions";

    public ThymeleafHstHeadContributionsTag(final String dialectPrefix) {
        super(dialectPrefix, TAG_NAME);
    }

    @Override
    protected void doProcess(final ITemplateContext context, final IModel model, final IElementModelStructureHandler structureHandler) {
        final String includes = getAttribute(model, "hst:categoryIncludes");
        final String excludes = getAttribute(model, "hst:categoryIncludes");
        final Boolean xhtmlAttribute = getAttributeExpression(context, model, "hst:xhtml");
        final boolean xhtml = xhtmlAttribute == null ? false: xhtmlAttribute;
        // remove tag:
        model.reset();
        HstHeadContributionHelper.INSTANCE.contributedHeadElements(includes, excludes, xhtml);


    }

}
