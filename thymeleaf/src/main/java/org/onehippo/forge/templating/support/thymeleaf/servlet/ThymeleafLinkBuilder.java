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
import org.hippoecm.hst.core.linking.HstLink;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.core.request.ResolvedMount;
import org.hippoecm.hst.util.HstRequestUtils;
import org.hippoecm.hst.util.PathUtils;
import org.hippoecm.hst.util.WebFileUtils;
import org.onehippo.cms7.services.HippoServiceRegistry;
import org.onehippo.cms7.services.webfiles.WebFileBundle;
import org.onehippo.cms7.services.webfiles.WebFileException;
import org.onehippo.cms7.services.webfiles.WebFilesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.context.WebEngineContext;
import org.thymeleaf.engine.TemplateData;
import org.thymeleaf.linkbuilder.ILinkBuilder;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.hippoecm.hst.utils.TagUtils.*;
import static org.onehippo.forge.templating.support.core.servlet.AbstractHstTemplateServlet.*;

public class ThymeleafLinkBuilder implements ILinkBuilder {

    private static final Logger log = LoggerFactory.getLogger(ThymeleafLinkBuilder.class);


    private final static String WEB_FILES_SITEMAP_ITEM_ID = "WEB-FILES-ID";
    private final static String WEB_FILE_NAMED_PIPELINE_NAME = "WebFilePipeline";

    @Override public String getName() {
        return "ThymeleafLinkBuilder";
    }

    @Override public Integer getOrder() {
        return 1;
    }

    @Override
    public String buildLink(final IExpressionContext context, final String base, final Map<String, Object> parameters) {
        if (base.startsWith("/")) {
            return base;
        }
        final WebEngineContext ctx = (WebEngineContext) context;
        final List<TemplateData> stack = ctx.getTemplateStack();
        if (stack == null || stack.isEmpty()) {
            return base;
        }

        final TemplateData templateData = stack.get(0);
        final String template = templateData.getTemplate();
        if (template != null && template.startsWith(WEB_FILE_TEMPLATE_PROTOCOL)) {
            try {
                return createWebfileLink(ctx, createAbsolutePath(stack, base));
            } catch (JspException e) {
                log.error("Error creating path", e);
            }
        }
        return base;
    }


    private String createAbsolutePath(final List<TemplateData> stack, final String base) {
        if (base.startsWith("/")) {
            return base;
        }
        String prefix = "";
        String path = "";
        for (TemplateData templateData : stack) {
            final String template = templateData.getTemplate();
            if (prefix.length() == 0 && template.startsWith(WEB_FILE_TEMPLATE_PROTOCOL)) {
                prefix = template.substring(WEB_FILE_TEMPLATE_PROTOCOL.length(), template.lastIndexOf('/'));
                path = prefix;
                continue;
            }
            if (template.startsWith("/")) {
                path = template.substring(0, template.lastIndexOf('/'));
            } else if (prefix.length() > 0) {
                if (template.indexOf('/') > 0) {
                    path = path + '/' + template.substring(0, template.lastIndexOf('/'));
                } else {
                    path = path.substring(0, path.lastIndexOf('/'));
                }
            }

        }
        return path + '/' + base;
    }

    private String createWebfileLink(final WebEngineContext ctx, final String path) throws JspException {


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
}
