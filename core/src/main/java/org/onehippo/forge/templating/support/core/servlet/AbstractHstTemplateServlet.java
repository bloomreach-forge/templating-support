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
package org.onehippo.forge.templating.support.core.servlet;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hippoecm.hst.core.container.ContainerConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractHstTemplateServlet extends HttpServlet {

    public static final long serialVersionUID = 1L;

    private static Logger log = LoggerFactory.getLogger(AbstractHstTemplateServlet.class);

    public static final String WEB_FILE_TEMPLATE_PROTOCOL = "webfile:";

    public static final String CLASSPATH_TEMPLATE_PROTOCOL = "classpath:";

    static final String CONTEXT_ATTRIBUTE_TEMPLATING_SERVLET_MAP = AbstractHstTemplateServlet.class.getName()
            + ".CONTEXT_ATTRIBUTE_TEMPLATING_SERVLET_MAP";

    @Override
    public final void init(ServletConfig config) throws ServletException {
        super.init(config);
        initializeTemplateEngine(config);

        final ServletContext servletContext = config.getServletContext();

        Map<String, AbstractHstTemplateServlet> templatingServletMap = (Map<String, AbstractHstTemplateServlet>) servletContext
                .getAttribute(CONTEXT_ATTRIBUTE_TEMPLATING_SERVLET_MAP);

        if (templatingServletMap == null) {
            templatingServletMap = new ConcurrentHashMap<>();
            servletContext.setAttribute(CONTEXT_ATTRIBUTE_TEMPLATING_SERVLET_MAP, templatingServletMap);
        }

        templatingServletMap.put(config.getServletName(), this);
    }

    protected abstract void initializeTemplateEngine(ServletConfig config) throws ServletException;

    @Override
    public final void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        process(request, response);
    }

    @Override
    public final void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String templatePath = getTemplatePath(request);
        log.debug("Requested template {}.", templatePath);
        Object templateContext = createTemplateContext(request, response);
        processTemplate(request, response, templatePath, templateContext);
    }

    protected abstract Object createTemplateContext(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;

    protected abstract void processTemplate(HttpServletRequest request, HttpServletResponse response,
            String templatePath, Object context) throws ServletException, IOException;

    protected void clearTemplateCache() {
    }

    protected String getTemplatePath(HttpServletRequest request) throws ServletException {
        final String protocol = (String) request.getAttribute(ContainerConstants.DISPATCH_URI_PROTOCOL);
        final String pathInfo = getTemplatePathInfo(request);
        return (protocol != null) ? protocol + pathInfo : pathInfo;
    }

    private String getTemplatePathInfo(HttpServletRequest request) throws ServletException {
        String includeServletPath = (String) request.getAttribute("javax.servlet.include.servlet_path");

        if (includeServletPath != null) {
            String includePathInfo = (String) request.getAttribute("javax.servlet.include.path_info");
            return includePathInfo == null ? includeServletPath : includePathInfo;
        }

        String path = request.getPathInfo();

        if (path != null) {
            return path;
        }

        path = request.getServletPath();

        if (path != null) {
            return path;
        }

        return "";
    }
}