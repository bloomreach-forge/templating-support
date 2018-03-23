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

import org.onehippo.forge.templating.support.core.helper.HstURLHelper;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;

public class ThymeleafHstRenderUrlAttribute extends ThymeleafHstAttribute {
    private static final String ATTR_NAME = "renderURL";


    public ThymeleafHstRenderUrlAttribute(final String dialectPrefix) {
        super(dialectPrefix, ATTR_NAME);
    }

    protected void doProcess(final ITemplateContext context, final IProcessableElementTag tag, final AttributeName attributeName, final String attributeValue, final IElementTagStructureHandler structureHandler) {
        final String link = HstURLHelper.INSTANCE.renderURL(attributeValue);
        setLink(structureHandler, tag, link);

    }

    



}
