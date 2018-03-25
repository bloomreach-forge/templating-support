/*
 * Copyright 2018 Hippo B.V. (http://www.onehippo.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *         http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onehippo.forge.templating.support.core.helper;

import org.apache.commons.lang.StringUtils;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.standard.HippoHtmlBean;
import org.hippoecm.hst.content.rewriter.ContentRewriter;
import org.hippoecm.hst.content.rewriter.ContentRewriterFactory;
import org.hippoecm.hst.content.rewriter.ImageVariant;
import org.hippoecm.hst.content.rewriter.impl.DefaultImageVariant;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.site.HstServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * HST Html Helper.
 */
public class HstHtmlHelper {

    private static final Logger log = LoggerFactory.getLogger(HstHtmlHelper.class);
    public static final HstHtmlHelper INSTANCE = new HstHtmlHelper();

    private HstHtmlHelper() {
    }

    public String htmlByHippoHtml(HippoHtmlBean htmlBean, boolean fullyQualified) {
        final HstRequestContext requestContext = RequestContextProvider.get();
        ContentRewriterFactory factory = HstServices.getComponentManager()
                .getComponent(ContentRewriterFactory.class.getName());
        ContentRewriter<String> contentRewriter = factory.createContentRewriter();
        contentRewriter.setFullyQualifiedLinks(fullyQualified);
        return contentRewriter.rewrite(htmlBean.getContent(), htmlBean.getNode(), requestContext);
    }
    
    public String htmlByHippoHtml(HippoHtmlBean htmlBean, final ContentRewriter<String> contentRewriter, final ImageVariant imageVariant,  final boolean canonicalLinks,  boolean fullyQualified) {
        final HstRequestContext requestContext = RequestContextProvider.get();
        contentRewriter.setCanonicalLinks(canonicalLinks);
        contentRewriter.setImageVariant(imageVariant);
        contentRewriter.setFullyQualifiedLinks(fullyQualified);
        return contentRewriter.rewrite(htmlBean.getContent(), htmlBean.getNode(), requestContext);
    }

    public String htmlByFormattedText(String formattedText, boolean fullyQualified) {
        final HstRequestContext requestContext = RequestContextProvider.get();
        ContentRewriterFactory factory = HstServices.getComponentManager()
                .getComponent(ContentRewriterFactory.class.getName());
        ContentRewriter<String> contentRewriter = factory.createContentRewriter();
        contentRewriter.setFullyQualifiedLinks(fullyQualified);
        return contentRewriter.rewrite(formattedText, requestContext);
    }

    public ImageVariant replaceVariants(final String name, final String replaces, final boolean fallback) {
        if (StringUtils.isBlank(name)) {
            log.warn("For imageVariant tag the name attribute is not allowed to be null or empty. Skip image variant");
            return null;
        } else {

            List<String> replaceVariants = new ArrayList<>();
            if (StringUtils.isNotBlank(replaces)) {
                if (replaces.contains(",")) {
                    String[] elems = replaces.split(",");
                    for (String elem : elems) {
                        if (StringUtils.isNotBlank(elem)) {
                            replaceVariants.add(elem);
                        }
                    }
                } else {
                    replaceVariants.add(replaces);
                }
            }
            return new DefaultImageVariant(name, replaceVariants, fallback);
        }

    }

    public  ContentRewriter<String> getOrCreateContentRewriter(final ContentRewriter<String> contentRewriter) {
        if (contentRewriter == null) {
            ContentRewriterFactory factory = HstServices.getComponentManager().getComponent(ContentRewriterFactory.class.getName());
            return factory.createContentRewriter();
        }
        return contentRewriter;
    }


}
