/*
 * Copyright 2018-2024 Bloomreach B.V. (http://www.bloomreach.com)
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
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.facetnavigation.HippoFacetSubNavigation;
import org.hippoecm.hst.core.linking.HstLink;
import org.hippoecm.hst.core.linking.HstLinkCreator;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.utils.TagUtils;
import org.onehippo.forge.templating.support.core.servlet.TemplateRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * HST Link Creation Helper.
 */
public class HstLinkHelper {

    private static final Logger log = LoggerFactory.getLogger(HstLinkHelper.class);
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

    public String linkForFacet(final HippoFacetSubNavigation current, final HippoFacetSubNavigation remove, final List<HippoFacetSubNavigation>removeList) {
        if (current == null || (remove == null && (removeList == null || removeList.isEmpty()))) {
            log.warn("Cannot remove a facet-value combi because 'current' or 'remove(List)' is null or empty");
            return null;
        }

        final HstRequestContext requestContext = RequestContextProvider.get();
        if (requestContext == null) {
            log.warn("There is no HstRequestContext. Cannot create an HstLink outside the hst request processing. Return");
            return null;
        }

        final HstLink link = requestContext.getHstLinkCreator().create(current.getNode(), requestContext, null, true, true);

        if (link == null || link.getPath() == null) {
            log.warn("Unable to rewrite link for '{}'. Return EVAL_PAGE", current.getPath());
            return null;
        }

        // now strip of the facet-value combi(s) that needs to be stripped of
        String path = link.getPath();

        final List<HippoFacetSubNavigation> combinedRemovedList = new ArrayList<HippoFacetSubNavigation>();
        if (removeList != null) {
            combinedRemovedList.addAll(removeList);
        }
        if (remove != null) {
            combinedRemovedList.add(remove);
        }

        for (HippoFacetSubNavigation toRemove : combinedRemovedList) {
            path = removeFacetKeyValueFromPath(path, toRemove.getFacetValueCombi().getKey(), toRemove.getFacetValueCombi().getValue());
        }
        link.setPath(path);
        String urlString = link.toUrlForm(requestContext, false);
        final HttpServletRequest servletRequest = requestContext.getServletRequest();
        // append again the current queryString as we are context relative
        if (servletRequest.getQueryString() != null && !"".equals(servletRequest.getQueryString())) {
            urlString += '?' + servletRequest.getQueryString();
        }

        return urlString;
    }

    private static String removeFacetKeyValueFromPath(final String path, final String facetKey, final String facetValue) {
        // assume the facetkey is 'a' and value is 'b' then from path,
        // only a/b should be removed at the beginning or at then end, and in the middle
        // hence we first at a leading and trailing slash, which we at the end remove again
        String modifiablePath = '/' + path + '/';
        String facetKeyValueRegExp = '/' + facetKey + '/' + facetValue + '/';

        // do not use String#replaceAll as that fails in case you need to replace /a/b/ from /a/b/a/b/c/d : In this case
        // only the first /a/b/ is matched and not the second
        while (modifiablePath.contains(facetKeyValueRegExp)) {
            modifiablePath = modifiablePath.replace(facetKeyValueRegExp, "/");
        }

        if (path.equals(modifiablePath)) {
            log.warn("Cannot remove '{}' from the current faceted navigation url '{}'.", facetKey + "/" + facetValue, path);
        }
        // remove again leading and trailing extra slashes
        return modifiablePath.substring(1, modifiablePath.length() - 1);
    }

}
