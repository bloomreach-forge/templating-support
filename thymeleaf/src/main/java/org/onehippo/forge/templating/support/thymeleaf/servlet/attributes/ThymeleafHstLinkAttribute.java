/*
 * Copyright 2018-2019 Bloomreach B.V. (http://www.bloomreach.com)
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

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.linking.HstLink;
import org.onehippo.forge.templating.support.core.helper.HstLinkHelper;
import org.onehippo.forge.templating.support.thymeleaf.servlet.utils.ThymeleafHstUtils;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IAttribute;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;

public class ThymeleafHstLinkAttribute extends BaseAttributeProcessor {
    private static final String ATTR_NAME = "link";

    public ThymeleafHstLinkAttribute(final String dialectPrefix) {
        super(dialectPrefix, ATTR_NAME);
    }

    @Override
    protected void doProcess(final ITemplateContext context, final IProcessableElementTag tag, final AttributeName attributeName, final String attributeValue, final IElementTagStructureHandler structureHandler) {
        final Object o = ThymeleafHstUtils.getExpression(context, attributeValue);
        final IAttribute attribute = tag.getAttribute(ThymeleafHstUtils.ATTR_FULLY_QUALIFIED);
        final boolean fullyQualified = parseBoolean(attribute);
        if (o instanceof HippoBean) {
            setLink(structureHandler, tag, HstLinkHelper.INSTANCE.linkByHippoBean((HippoBean) o, fullyQualified));
        } else if (o instanceof HstLink) {
            final HstLink link = (HstLink) o;
            setLink(structureHandler, tag, link.toUrlForm(RequestContextProvider.get(), fullyQualified));
        } else if (o instanceof String) {
            setLink(structureHandler, tag, HstLinkHelper.INSTANCE.linkByPath((String) o, fullyQualified));
        }


    }


}
