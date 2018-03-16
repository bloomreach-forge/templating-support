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

package org.onehippo.forge.templating.support.thymeleaf.servlet;

import org.hippoecm.hst.configuration.HstNodeTypes;
import org.hippoecm.hst.configuration.site.HstSite;
import org.hippoecm.hst.configuration.sitemap.HstSiteMapItem;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.hippoecm.hst.content.rewriter.ContentRewriter;
import org.hippoecm.hst.content.rewriter.ContentRewriterFactory;
import org.hippoecm.hst.core.linking.HstLink;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.core.request.ResolvedMount;
import org.hippoecm.hst.site.HstServices;
import org.hippoecm.hst.util.HstRequestUtils;
import org.hippoecm.hst.util.PathUtils;
import org.hippoecm.hst.util.WebFileUtils;
import org.onehippo.cms7.services.HippoServiceRegistry;
import org.onehippo.cms7.services.webfiles.WebFileBundle;
import org.onehippo.cms7.services.webfiles.WebFileException;
import org.onehippo.cms7.services.webfiles.WebFilesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.WebEngineContext;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import java.util.Collections;

import static org.hippoecm.hst.utils.TagUtils.*;

public final class ThymeleafUtils {

    private static final Logger log = LoggerFactory.getLogger(ThymeleafUtils.class);
    private final static String WEB_FILES_SITEMAP_ITEM_ID = "WEB-FILES-ID";
    private final static String WEB_FILE_NAMED_PIPELINE_NAME = "WebFilePipeline";

    private ThymeleafUtils() {
    }

    public static String createHstLink(final WebEngineContext ctx, final HippoBean bean) {
        if (bean == null) {
            return "";
        }
        final HttpServletRequest servletRequest = ctx.getRequest();
        final HstRequestContext context = HstRequestUtils.getHstRequestContext(servletRequest);
        final HstLink hstLink = context.getHstLinkCreator().create(bean, context);
        if (hstLink == null) {
            return "";
        }
        return hstLink.toUrlForm(context, false);

    }

    public static String createWebfileLinkSilent(final WebEngineContext ctx, final String path)  {
        try {
            return createWebfileLink(ctx, path);
        } catch (JspException ignore) {
            // ignore
        }
        return "";
    }

    // NOTE: forked from HST webfiles link tag, removed JSP related code...
    public static String createWebfileLink(final WebEngineContext ctx, final String path) throws JspException {


        final HttpServletRequest servletRequest = ctx.getRequest();
        final HstRequestContext reqContext = HstRequestUtils.getHstRequestContext(servletRequest);

        final String webFilePath = PathUtils.normalizePath(path);

        final HstSite hstSite;
        if (reqContext == null || (hstSite = reqContext.getResolvedMount().getMount().getHstSite()) == null || hstSite.getSiteMap() == null) {
            log.debug("Although there is no HstRequestContext/HstSite/Sitemap for request, a link for path='{}' is created " +
                    "similar to how the c:url tag would do it", webFilePath);

            return createPathInfoWithoutRequestContext(webFilePath, Collections.emptyMap(), Collections.emptyList(), servletRequest);
        }

        final HstSiteMapItem webFileSiteMapItem = hstSite.getSiteMap().getSiteMapItemByRefId(WEB_FILES_SITEMAP_ITEM_ID);

        if (webFileSiteMapItem == null || !WEB_FILE_NAMED_PIPELINE_NAME.equals(webFileSiteMapItem.getNamedPipeline())) {
            log.warn("Cannot create web file link for site '{}' because it does not have a sitemap " +
                            "that contains a sitemap item with properties '{} = {}' and '{} = {}'",
                    hstSite.getConfigurationPath(), HstNodeTypes.SITEMAPITEM_PROPERTY_REF_ID, WEB_FILES_SITEMAP_ITEM_ID,
                    HstNodeTypes.SITEMAPITEM_PROPERTY_NAMEDPIPELINE, WEB_FILE_NAMED_PIPELINE_NAME);
            return path;
        }

        // check whether no wildcards presents in webFileSiteMapItem or ancestor
        HstSiteMapItem current = webFileSiteMapItem;
        StringBuilder webFilesPrefix = new StringBuilder("/");
        while (current != null) {
            if (current.containsAny() || current.containsWildCard() ||
                    current.isAny() || current.isWildCard()) {
                log.warn("Cannot create web file link for site '{}' because the sitemap item " +
                                "for the web file '{}' contains wildcards (or one of its parents).",
                        hstSite.getConfigurationPath(), webFileSiteMapItem.getQualifiedId());
                return path;
            }
            webFilesPrefix.insert(0, current.getValue()).insert(0, '/');
            current = current.getParentItem();
        }

        final WebFilesService service = HippoServiceRegistry.getService(WebFilesService.class);
        if (service == null) {
            log.error("Missing service for '{}'. Cannot create web file url.", WebFilesService.class.getName());
            return path;
        }

        final ResolvedMount resolvedMount = reqContext.getResolvedMount();
        try {
            final Session session = reqContext.getSession();

            final String bundleName = WebFileUtils.getBundleName(reqContext);
            try {
                final WebFileBundle webFileBundle = service.getJcrWebFileBundle(session, bundleName);
                webFilesPrefix.append(webFileBundle.getAntiCacheValue());
            } catch (WebFileException e) {
                if (log.isDebugEnabled()) {
                    log.warn("Cannot find web file bundle '{}'", bundleName, e);
                } else {
                    log.info("Cannot find web file bundle '{}' : {}", bundleName, e.toString());
                }
                return path;
            }
        } catch (RepositoryException e) {
            log.error("Exception while trying to retrieve the node path for the edit location", e);
            return path;
        }

        final String fullWebFilePath = webFilesPrefix.append('/').append(webFilePath).toString();
        final HstLink link = reqContext.getHstLinkCreator().create(fullWebFilePath, resolvedMount.getMount(), true);


        return link.toUrlForm(reqContext, false);
    }

    public static String extractHtml(final WebEngineContext ctx, final HippoHtml bean) {
        if (bean == null) {
            return "";
        }
        // TODO improve...
        final String content = bean.getContent();
        ContentRewriterFactory factory = HstServices.getComponentManager().getComponent(ContentRewriterFactory.class.getName());
        final ContentRewriter<String> contentRewriter = factory.createContentRewriter();
        return contentRewriter.rewrite(content, RequestContextProvider.get());
    }
}
