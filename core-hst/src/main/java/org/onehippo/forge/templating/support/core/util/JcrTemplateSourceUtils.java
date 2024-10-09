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
package org.onehippo.forge.templating.support.core.util;

import java.io.IOException;
import java.io.InputStream;

import javax.jcr.Binary;
import javax.jcr.Credentials;
import javax.jcr.Item;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.io.IOUtils;
import org.hippoecm.hst.configuration.HstNodeTypes;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.site.HstServices;
import org.hippoecm.hst.util.PathUtils;
import org.hippoecm.hst.util.WebFileUtils;
import org.hippoecm.repository.util.JcrUtils;
import org.onehippo.forge.templating.support.core.servlet.AbstractHstTemplateServlet;
import org.onehippo.repository.util.JcrConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Various utilities to handle template sources stored in JCR.
 */
public class JcrTemplateSourceUtils {

    private static Logger log = LoggerFactory.getLogger(JcrTemplateSourceUtils.class);

    /**
     * An allowed template source binary data's mime type in Hippo Repository.
     */
    private final static String TEMPLATE_BINARY_MIMETYPE = "application/octet-stream";

    /**
     * An allowed template source binary data's mime type in Hippo Repository.
     */
    private final static String HTML_TEMPLATE_MIMETYPE = "text/html";

    private JcrTemplateSourceUtils() {
    }

    /**
     * Convert the given logical {@code templateLocation} to a JCR webfile node path.
     * @param templateLocation logical template location
     * @return A JCR webfile node path converted from the given logical {@code templateLocation}
     */
    public static String getWebFileJcrPath(final String templateLocation) {
        if (templateLocation == null) {
            return null;
        }

        if (templateLocation.startsWith(AbstractHstTemplateServlet.WEB_FILE_TEMPLATE_PROTOCOL)) {
            return WebFileUtils.webFilePathToJcrPath(templateLocation, WebFileUtils.getBundleName(RequestContextProvider.get()));
        }

        return "/webfiles" + "/" + WebFileUtils.getBundleName(RequestContextProvider.get()) + "/"
                + PathUtils.normalizePath(templateLocation);
    }

    /**
     * Read the JCR node or property designated by the given {@code jcrTemplatePath} and return its content as string.
     * @param jcrTemplatePath JCR template node or property path
     * @return String content from the JCR node or property designated by the given {@code jcrTemplatePath}
     * @throws RepositoryException if repository exception occurs
     */
    public static String getTemplateSourceContent(final String jcrTemplatePath) throws RepositoryException {
        Session session = null;

        try {
            session = createTemplateReaderSession();

            if (session.nodeExists(jcrTemplatePath)) {
                Node templateNode = session.getNode(jcrTemplatePath);
                return JcrTemplateSourceUtils.getTemplateSourceContent(templateNode);
            }
        } catch (RepositoryException e) {
            log.warn("Cannot load template source at {}.", jcrTemplatePath, e);
        } finally {
            if (session != null) {
                session.logout();
            }
        }

        return null;
    }

    /**
     * Read the JCR node or property, {@code templateItem}, and return its content as string.
     * @param templateItem the JCR node or property containing the template content
     * @return String content from the JCR node or property containing the template content
     * @throws RepositoryException if repository exception occurs
     */
    public static String getTemplateSourceContent(final Item templateItem) throws RepositoryException {
        if (templateItem.isNode()) {
            final Node templateNode = (Node) templateItem;

            if (templateNode.isNodeType(JcrConstants.NT_FILE)) {
                return getTemplateSourceContentFromFileNode(templateNode);
            } else {
                return JcrUtils.getStringProperty(templateNode, HstNodeTypes.TEMPLATE_PROPERTY_SCRIPT, "");
            }
        } else {
            Property templateProp = (Property) templateItem;
            return templateProp.getValue().getString();
        }
    }

    private static String getTemplateSourceContentFromFileNode(final Node templateNode) throws RepositoryException {
        final Node contentNode = templateNode.getNode(JcrConstants.JCR_CONTENT);
        final String path = templateNode.getPath();
        final String mimeType = JcrUtils.getStringProperty(contentNode, JcrConstants.JCR_MIME_TYPE, null);

        if (!TEMPLATE_BINARY_MIMETYPE.equals(mimeType) && !HTML_TEMPLATE_MIMETYPE.equals(mimeType)) {
            log.warn(
                    "Expected template binary or HTML at '{}' with mimetype '{}' or '{}' but was '{}'. Cannot return "
                            + "ftl for wrong mimetype",
                    path, TEMPLATE_BINARY_MIMETYPE, HTML_TEMPLATE_MIMETYPE, mimeType);
            return null;
        }

        final Binary templateBinary = JcrUtils.getBinaryProperty(contentNode, JcrConstants.JCR_DATA, null);

        if (templateBinary == null) {
            log.warn(
                    "Expected template binary at '{}' but binary was null. Cannot return template source for wrong mimetype",
                    path);
            return null;
        }

        InputStream input = null;

        try {
            input = templateBinary.getStream();

            try {
                return IOUtils.toString(input, "UTF-8");
            } catch (IOException e) {
                log.warn("Exception while reading freemarker binary from '{}'", path, e);
            }
        } finally {
            IOUtils.closeQuietly(input);
            templateBinary.dispose();
        }

        return null;
    }

    private static Session createTemplateReaderSession() throws RepositoryException {
        Repository repository = HstServices.getComponentManager().getComponent("javax.jcr.Repository.delegating");
        Credentials creds = HstServices.getComponentManager()
                .getComponent("javax.jcr.Credentials.hstconfigreader.delegating");
        return repository.login(creds);
    }
}
