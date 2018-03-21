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

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.standard.HippoHtmlBean;
import org.hippoecm.hst.content.rewriter.ContentRewriter;
import org.hippoecm.hst.content.rewriter.ContentRewriterFactory;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.site.HstServices;

/**
 * HST Html Helper.
 */
public class HstHtmlHelper {

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

    public String htmlByFormattedText(String formattedText, boolean fullyQualified) {
        final HstRequestContext requestContext = RequestContextProvider.get();
        ContentRewriterFactory factory = HstServices.getComponentManager()
                .getComponent(ContentRewriterFactory.class.getName());
        ContentRewriter<String> contentRewriter = factory.createContentRewriter();
        contentRewriter.setFullyQualifiedLinks(fullyQualified);
        return contentRewriter.rewrite(formattedText, requestContext);
    }
}
