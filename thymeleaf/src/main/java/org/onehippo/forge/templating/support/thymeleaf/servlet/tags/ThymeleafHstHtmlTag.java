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

import com.google.common.base.Strings;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.hippoecm.hst.content.rewriter.ContentRewriter;
import org.hippoecm.hst.content.rewriter.ImageVariant;
import org.onehippo.forge.templating.support.core.helper.HstHtmlHelper;
import org.onehippo.forge.templating.support.thymeleaf.servlet.utils.ThymeleafHstUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IModel;
import org.thymeleaf.processor.element.IElementModelStructureHandler;

import static org.onehippo.forge.templating.support.thymeleaf.servlet.utils.ThymeleafHstUtils.*;

public class ThymeleafHstHtmlTag extends BaseModelProcessor {
    private static final String TAG_NAME = "html";

    private static final Logger log = LoggerFactory.getLogger(ThymeleafHstHtmlTag.class);

    public ThymeleafHstHtmlTag(final String dialectPrefix) {
        super(dialectPrefix, TAG_NAME);
    }

    @Override
    protected void doProcess(final ITemplateContext context, final IModel model, final IElementModelStructureHandler structureHandler) {
        final String bean = getAttribute(model, "hst:htmlBean");
        final HippoHtml htmlBean = ThymeleafHstUtils.getExpression(context, bean);
        final boolean fullyQualified = getBooleanOrExpression(context, model, ATTR_FULLY_QUALIFIED);
        final String imageVariantName = getAttributeOrExpression(context, model, "hst:imageVariantName");
        final String imageVariantReplaces = getAttributeOrExpression(context, model, "hst:imageVariantReplaces");
        final boolean fallback = getBooleanOrExpression(context, model, "hst:imageVariantFallback");
        final boolean canonicalLinks = getBooleanOrExpression(context, model, "hst:canonicalLinks");
        final ContentRewriter<String> rewriter = getAttributeExpression(context, model, "hst:contentRewriter");
        final ContentRewriter<String> contentRewriter = HstHtmlHelper.INSTANCE.getOrCreateContentRewriter(rewriter);
        final ImageVariant imageVariant = Strings.isNullOrEmpty(imageVariantName) ? null : HstHtmlHelper.INSTANCE.replaceVariants(imageVariantName, imageVariantReplaces, fallback);
        final String html = HstHtmlHelper.INSTANCE.rewriteHippoHtml(contentRewriter, htmlBean, imageVariant, canonicalLinks, fullyQualified);
        model.reset();
        if (!Strings.isNullOrEmpty(html)) {
            model.insert(0, new TextEvent(html));
        }

    }

}
