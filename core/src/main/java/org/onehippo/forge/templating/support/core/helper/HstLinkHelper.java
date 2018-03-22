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

import java.util.Collections;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.linking.HstLink;
import org.hippoecm.hst.core.linking.HstLinkCreator;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.utils.TagUtils;
import org.onehippo.forge.templating.support.core.servlet.TemplateRequestContext;

/**
 * HST Link Creation Helper.
 */
public class HstLinkHelper {

    public static final HstLinkHelper INSTANCE = new HstLinkHelper();

    private HstLinkHelper() {
    }

    public String linkByPath(String path, boolean fullyQualified) {
        final HstRequestContext requestContext = RequestContextProvider.get();

        if (requestContext == null) {
            try {
                return TagUtils.createPathInfoWithoutRequestContext(path, Collections.emptyMap(),
                        Collections.emptyList(), TemplateRequestContext.getRequest());
            } catch (Exception e) {
                throw new RuntimeException(e.toString(), e.getCause());
            }
        }

        final HstLinkCreator linkCreator = requestContext.getHstLinkCreator();
        final HstLink hstLink = linkCreator.create(path, requestContext.getResolvedMount().getMount());
        return hstLink.toUrlForm(requestContext, fullyQualified);
    }

    public String linkByHippoBean(HippoBean hippoBean, boolean fullyQualified) {
        final HstRequestContext requestContext = RequestContextProvider.get();
        final HstLinkCreator linkCreator = requestContext.getHstLinkCreator();
        final HstLink hstLink = linkCreator.create(hippoBean, requestContext);
        return hstLink.toUrlForm(requestContext, fullyQualified);
    }

    public String linkBySiteMapItemRefId(String siteMapItemRefId, boolean fullyQualified) {
        final HstRequestContext requestContext = RequestContextProvider.get();
        final HstLinkCreator linkCreator = requestContext.getHstLinkCreator();
        final HstLink hstLink = linkCreator.createByRefId(siteMapItemRefId, requestContext.getResolvedMount().getMount());
        return hstLink.toUrlForm(requestContext, fullyQualified);
    }
}
