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

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.hippoecm.hst.configuration.HstNodeTypes;
import org.hippoecm.hst.configuration.site.HstSite;
import org.hippoecm.hst.configuration.sitemap.HstSiteMapItem;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.core.linking.HstLink;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.util.PathUtils;
import org.hippoecm.hst.util.WebFileUtils;
import org.onehippo.cms7.services.HippoServiceRegistry;
import org.onehippo.cms7.services.webfiles.WebFileBundle;
import org.onehippo.cms7.services.webfiles.WebFileException;
import org.onehippo.cms7.services.webfiles.WebFilesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HST Webfiles Helper.
 */
public class HstWebfilesHelper {

    private static Logger log = LoggerFactory.getLogger(HstWebfilesHelper.class);

    public static final HstWebfilesHelper INSTANCE = new HstWebfilesHelper();

    private final static String WEB_FILES_SITEMAP_ITEM_ID = "WEB-FILES-ID";

    private final static String WEB_FILE_NAMED_PIPELINE_NAME = "WebFilePipeline";

    private HstWebfilesHelper() {
    }

    public String webfileByPath(String path, boolean fullyQualified) {
        final HstRequestContext requestContext = RequestContextProvider.get();
        final String fullWebFilePath = findFullWebFilePath(requestContext, path);

        if (fullWebFilePath == null) {
            return null;
        }

        final HstLink link = requestContext.getHstLinkCreator().create(fullWebFilePath,
                requestContext.getResolvedMount().getMount(), true);
        return link.toUrlForm(requestContext, fullyQualified);
    }

    private String findFullWebFilePath(final HstRequestContext requestContext, String path) {
        final String webFilePath = PathUtils.normalizePath(path);
        final HstSite hstSite = requestContext.getResolvedMount().getMount().getHstSite();
        final HstSiteMapItem webFileSiteMapItem = hstSite.getSiteMap().getSiteMapItemByRefId(WEB_FILES_SITEMAP_ITEM_ID);

        if (webFileSiteMapItem == null || !WEB_FILE_NAMED_PIPELINE_NAME.equals(webFileSiteMapItem.getNamedPipeline())) {
            log.warn(
                    "Cannot create web file link for site '{}' because it does not have a sitemap "
                            + "that contains a sitemap item with properties '{} = {}' and '{} = {}'",
                    hstSite.getConfigurationPath(), HstNodeTypes.SITEMAPITEM_PROPERTY_REF_ID, WEB_FILES_SITEMAP_ITEM_ID,
                    HstNodeTypes.SITEMAPITEM_PROPERTY_NAMEDPIPELINE, WEB_FILE_NAMED_PIPELINE_NAME);
            return null;
        }

        HstSiteMapItem current = webFileSiteMapItem;
        StringBuilder webFilesPrefix = new StringBuilder("/");

        while (current != null) {
            if (current.containsAny() || current.containsWildCard() || current.isAny() || current.isWildCard()) {
                log.warn(
                        "Cannot create web file link for site '{}' because the sitemap item "
                                + "for the web file '{}' contains wildcards (or one of its parents).",
                        hstSite.getConfigurationPath(), webFileSiteMapItem.getQualifiedId());
                return null;
            }

            webFilesPrefix.insert(0, current.getValue()).insert(0, "/");
            current = current.getParentItem();
        }

        WebFilesService service = HippoServiceRegistry.getService(WebFilesService.class);

        try {
            final Session session = requestContext.getSession();
            final String bundleName = WebFileUtils.getBundleName(requestContext);

            try {
                final WebFileBundle webFileBundle = service.getJcrWebFileBundle(session, bundleName);
                webFilesPrefix.append(webFileBundle.getAntiCacheValue());
            } catch (WebFileException e) {
                log.warn("Cannot find web file bundle '{}'", bundleName, e);
                return null;
            }
        } catch (RepositoryException e) {
            log.error("Exception while trying to retrieve the node path for the edit location", e);
            return null;
        }

        return webFilesPrefix.append("/").append(webFilePath).toString();
    }
}
