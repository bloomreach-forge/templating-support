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

package org.onehippo.forge.templating.support.thymeleaf.servlet.tags;

import org.onehippo.forge.templating.support.core.helper.HstSetBundleHelper;
import org.onehippo.forge.templating.support.thymeleaf.servlet.attributes.ThymeleafHstHtmlAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IModel;
import org.thymeleaf.processor.element.IElementModelStructureHandler;

public class ThymeleafHstSetBundleTag extends BaseModelProcessor {
    private static final String TAG_NAME = "setBundle";

    private static final Logger log = LoggerFactory.getLogger(ThymeleafHstHtmlAttribute.class);

    public ThymeleafHstSetBundleTag(final String dialectPrefix) {
        super(dialectPrefix, TAG_NAME);
    }

    @Override
    protected void doProcess(final ITemplateContext context, final IModel model, final IElementModelStructureHandler structureHandler) {
        final String baseName = getAttributeExpression(context, model, "hst:basename");
        final boolean fallback = getBooleanOrExpression(context, model, "hst:fallbackToJavaResourceBundle");
        HstSetBundleHelper.INSTANCE.setBundle(baseName, fallback);
        // don't render tag
        model.reset();
    }

}
