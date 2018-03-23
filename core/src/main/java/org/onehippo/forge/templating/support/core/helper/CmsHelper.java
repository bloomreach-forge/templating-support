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
package org.onehippo.forge.templating.support.core.helper;

import org.apache.commons.lang.StringUtils;
import org.hippoecm.hst.configuration.HstNodeTypes;
import org.hippoecm.hst.configuration.hosting.Mount;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.channelmanager.ChannelManagerConstants;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.util.EncodingUtils;
import org.hippoecm.hst.util.HstRequestUtils;
import org.hippoecm.repository.api.HippoNode;
import org.hippoecm.repository.api.HippoNodeType;
import org.onehippo.forge.templating.support.core.servlet.TemplateRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hippoecm.hst.utils.TagUtils.*;

/**
 * For CMS related tags
 */
public final class CmsHelper {

    private static final Logger log = LoggerFactory.getLogger(CmsHelper.class);
    public static final CmsHelper INSTANCE = new CmsHelper();

    // empty string or null?
    public static final String EMPTY_RESULT = "";

    private CmsHelper() {
    }


    public String createCmsEditLinkAsComment(final HippoBean bean) {
        return createCmsEditLink(bean, true);
    }
    public String createCmsEditLinkAsLink(final HippoBean bean) {
        return createCmsEditLink(bean, false);
    }
    private String createCmsEditLink(final HippoBean bean, final boolean asComment) {
        final HstRequestContext requestContext = RequestContextProvider.get();
        if (invalid(bean, requestContext)) {
            return "";
        }

        final Mount mount = requestContext.getResolvedMount().getMount();

        // cmsBaseUrl is something like : http://localhost:8080
        // try to find find the best cms location in case multiple ones are configured
        if (mount.getCmsLocations().isEmpty()) {
            log.warn("Skipping cms edit url no cms locations configured in hst hostgroup configuration");
            return EMPTY_RESULT;
        }
        final HttpServletRequest request = TemplateRequestContext.getRequest();
        String cmsBaseUrl;
        if (mount.getCmsLocations().size() == 1) {
            cmsBaseUrl = mount.getCmsLocations().get(0);
        } else {
            cmsBaseUrl = getBestCmsLocation(mount.getCmsLocations(), HstRequestUtils.getFarthestRequestHost(request, false));
        }

        if (cmsBaseUrl.endsWith("/")) {
            cmsBaseUrl = cmsBaseUrl.substring(0, cmsBaseUrl.length() - 1);
        }

        HippoNode node = (HippoNode) bean.getNode();
        String nodeLocation;
        String nodeId;
        try {
            Node editNode = node.getCanonicalNode();
            if (editNode == null) {
                log.debug("Cannot create a 'surf and edit' link for a pure virtual jcr node: '{}'", node.getPath());
                return EMPTY_RESULT;
            } else {
                Node rootNode = editNode.getSession().getRootNode();
                if (editNode.isSame(rootNode)) {
                    log.warn("Cannot create a 'surf and edit' link for a jcr root node.");
                }
                if (editNode.isNodeType(HstNodeTypes.NODETYPE_HST_SITES)) {
                    log.warn("Cannot create a 'surf and edit' link for a jcr node of type '{}'.", HstNodeTypes.NODETYPE_HST_SITES);
                }
                if (editNode.isNodeType(HstNodeTypes.NODETYPE_HST_SITE)) {
                    log.warn("Cannot create a 'surf and edit' link for a jcr node of type '{}'.", HstNodeTypes.NODETYPE_HST_SITE);
                }

                Node handleNode = getHandleNodeIfIsAncestor(editNode, rootNode);
                if (handleNode != null) {
                    // take the handle node as this is the one expected by the cms edit url:
                    editNode = handleNode;
                    log.debug("The nodepath for the edit link in cms is '{}'", editNode.getPath());
                }
                nodeId = editNode.getIdentifier();
                nodeLocation = editNode.getPath();
                log.debug("The nodepath for the edit link in cms is '{}'", nodeLocation);

            }
        } catch (RepositoryException e) {
            log.error("Exception while trying to retrieve the node path for the edit location", e);
            return EMPTY_RESULT;
        }

        if (nodeLocation == null) {
            log.warn("Did not find a jcr node location for the bean to create a cms edit location with. ");
            return EMPTY_RESULT;
        }
        String encodedPath = EncodingUtils.getEncodedPath(nodeLocation, request);
        String cmsEditLink = cmsBaseUrl + "?path=" + encodedPath;
        if (asComment) {
            return encloseInHTMLComment(toJSONMap(getAttributeMap(cmsEditLink, nodeId)));
        }
        return cmsEditLink;
    }


    public String createManageContentComment(final HippoBean bean) {
        final HstRequestContext requestContext = RequestContextProvider.get();
        if (invalid(bean, requestContext)) {
            return EMPTY_RESULT;
        }

        return EMPTY_RESULT;
    }

    public String createCmsEditMenuLink(final HippoBean bean) {
        final HstRequestContext requestContext = RequestContextProvider.get();
        if (invalid(bean, requestContext)) {
            return EMPTY_RESULT;
        }

        return EMPTY_RESULT;
    }

    private boolean invalid(final HippoBean bean, final HstRequestContext requestContext) {
        return requestContext == null || !requestContext.isCmsRequest() || bean == null;
    }


    private Map<?, ?> getAttributeMap(final String url, final String nodeId) {
        final Map<String, Object> result = new HashMap<>();
        result.put(ChannelManagerConstants.HST_TYPE, "CONTENT_LINK");
        result.put("uuid", nodeId);
        result.put("url", url);
        return result;
    }

    private Node getHandleNodeIfIsAncestor(Node currentNode, Node rootNode) throws RepositoryException {
        if (currentNode.isNodeType(HippoNodeType.NT_HANDLE)) {
            return currentNode;
        }
        if (currentNode.isSame(rootNode)) {
            return null;
        }
        return getHandleNodeIfIsAncestor(currentNode.getParent(), rootNode);
    }

    private String getBestCmsLocation(final List<String> cmsLocations, final String cmsRequestHostName) {
        for (String cmsLocation : cmsLocations) {
            String hostName = cmsLocation;
            if (cmsLocation.startsWith("http://")) {
                hostName = hostName.substring("http://".length());
            } else if (cmsLocation.startsWith("https://")) {
                hostName = hostName.substring("https://".length());
            }
            hostName = StringUtils.substringBefore(hostName, "/");
            if (cmsRequestHostName.equals(hostName)) {
                log.debug("For cms request with host {} found from {} best cms host to be {}", cmsRequestHostName, cmsLocations, cmsLocation);
                return cmsLocation;
            }
        }
        log.debug("For cms request with host {} no matching host was found in {}. Return {} as cms host.", cmsRequestHostName,
                cmsLocations, cmsLocations.get(0));
        return cmsLocations.get(0);
    }
}
