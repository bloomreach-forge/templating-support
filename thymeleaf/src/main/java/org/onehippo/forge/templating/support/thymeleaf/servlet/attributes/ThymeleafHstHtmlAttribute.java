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

import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.hippoecm.hst.content.rewriter.ContentRewriter;
import org.hippoecm.hst.content.rewriter.ImageVariant;
import org.onehippo.forge.templating.support.core.helper.HstHtmlHelper;
import org.onehippo.forge.templating.support.thymeleaf.servlet.HstThymeleafUtils;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IAttribute;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;

import static org.onehippo.forge.templating.support.thymeleaf.servlet.HstThymeleafUtils.*;

public class ThymeleafHstHtmlAttribute extends BaseAttributeProcessor {
    private static final String ATTR_NAME = "html";

    public ThymeleafHstHtmlAttribute(final String dialectPrefix) {
        super(dialectPrefix, ATTR_NAME);
    }

    protected void doProcess(final ITemplateContext context, final IProcessableElementTag tag, final AttributeName attributeName, final String attributeValue, final IElementTagStructureHandler structureHandler) {
        final HippoHtml htmlBean = HstThymeleafUtils.getExpression(context, attributeValue);
        final IAttribute fullyQualifiedAttribute = tag.getAttribute(ATTR_FULLY_QUALIFIED);
        final String imageVariantName = getAttribute(tag, "hst:imageVariantName");
        final String imageVariantReplaces = getAttribute(tag, "hst:imageVariantReplaces");
        final boolean fallback = parseBoolean(tag.getAttribute("hst:imageVariantFallback"));
        final ContentRewriter<String> contentRewriter = HstHtmlHelper.INSTANCE.getOrCreateContentRewriter(getExpression(context, "hst:contentRewriter"));
        final ImageVariant imageVariant = HstHtmlHelper.INSTANCE.replaceVariants(imageVariantName, imageVariantReplaces, fallback);
        final boolean fullyQualified = parseBoolean(fullyQualifiedAttribute);
        final String html = HstHtmlHelper.INSTANCE.htmlByHippoHtml(htmlBean, contentRewriter, imageVariant, fullyQualified);
        structureHandler.setBody(html, false);
    }


}
