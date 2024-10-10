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
package org.onehippo.forge.templating.support.core.servlet;

import org.hippoecm.hst.core.container.ContainerConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Base Abstract Templating Support Servlet class which can be extended by a specific servlet implementation
 * for a different template technology to substitute for the default HstFreeMarker used in Hippo CMS Delivery tier
 * web application.
 * <p>
 * This base abstract servlet exists to support the following:
 * <ul>
 * <li>To define the methods that must or should be implemented by subclass implementations.
 * e.g, {@link #initializeTemplateEngine(ServletConfig)} to give the subclass a chance to initialize its own
 * templating engine on initialization phase, {@link #getTemplatePath(HttpServletRequest)} to give the subclass
 * an easy and safe way to retrieve the dispatched template path, {@link #createTemplateContext(HttpServletRequest, HttpServletResponse)}
 * to give the subclass a chance to construct a <i>Context</i> object to be evaluated by the specific templating engine,
 * and {@link #processTemplate(HttpServletRequest, HttpServletResponse, String, Object)} to let the subclass
 * finally to process the request with the created <i>Context</i> object on the template path.
 * <li>To define the common string constants such as "webfile:", "classpath", etc.
 * <li>To provide a generic JCR Observation event listener mechanism to <b>invalidate</b> the template cache
 * on any template configurations and webfiles changes, so subclasses do not have to worry about implementing
 * the JCR observation event listener by itself as long as it is okay to invalidate every cache item on any change,
 * which is almost not a problem in production where you don't update those configurations and webfiles at runtime,
 * and which is still very beneficial during development phase locally.
 * </ul>
 */
public abstract class AbstractHstTemplateServlet extends HttpServlet {

    public static final long serialVersionUID = 1L;

    private static Logger log = LoggerFactory.getLogger(AbstractHstTemplateServlet.class);

    /**
     * Webfile template protocol, used in HST template render path configurations.
     */
    public static final String WEB_FILE_TEMPLATE_PROTOCOL = "webfile:";

    /**
     * Classpath template protocol, used in HST template render path configurations.
     */
    public static final String CLASSPATH_TEMPLATE_PROTOCOL = "classpath:";

    /**
     * The servlet context attribute name, by which all the servlet instances extending this class are stored
     * in a map. Those registered servlet instances are invoked on {@link #clearTemplateCache()} whenever JCR Observation
     * event occurs on HST template configuration change or webfile item change, in order to clear the specific
     * template cache instance in a subclass.
     */
    static final String CONTEXT_ATTRIBUTE_TEMPLATING_SERVLET_MAP = AbstractHstTemplateServlet.class.getName()
            + ".CONTEXT_ATTRIBUTE_TEMPLATING_SERVLET_MAP";

    @Override
    public final void init(ServletConfig config) throws ServletException {
        super.init(config);
        initializeTemplateEngine(config);

        final ServletContext servletContext = config.getServletContext();

        @SuppressWarnings("unchecked")
        Map<String, AbstractHstTemplateServlet> templatingServletMap = (Map<String, AbstractHstTemplateServlet>) servletContext
                .getAttribute(CONTEXT_ATTRIBUTE_TEMPLATING_SERVLET_MAP);

        if (templatingServletMap == null) {
            templatingServletMap = new ConcurrentHashMap<>();
            servletContext.setAttribute(CONTEXT_ATTRIBUTE_TEMPLATING_SERVLET_MAP, templatingServletMap);
        }

        templatingServletMap.put(config.getServletName(), this);
    }

    /**
     * Initialize the specific templating engine in a subclass. This method is invoked by {@link #init(ServletConfig)}
     * method during the initialization phase.
     * @param config ServletConfig object
     * @throws ServletException if servlet exception occurs
     */
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
        try {
            TemplateRequestContext.reset(request, response);

            String templatePath = getTemplatePath(request);
            log.debug("Requested template {}.", templatePath);
            Object templateContext = createTemplateContext(request, response);
            processTemplate(request, response, templatePath, templateContext);
        } finally {
            TemplateRequestContext.clear();
        }
    }

    /**
     * Create a templating engine specific <i>Context</i> object which can be evaluated by the template.
     * <p>
     * This method is invoked by {@link #process(HttpServletRequest, HttpServletResponse)} method before invoking
     * {@link #processTemplate(HttpServletRequest, HttpServletResponse, String, Object)} to pass a <i>Context</i>
     * object to the {@link #processTemplate(HttpServletRequest, HttpServletResponse, String, Object)}.
     * @param request HttpServletRequest instance
     * @param response HttpServletResponse instance
     * @return a <i>Context</i> object which can be evaluated by the template
     * @throws ServletException if servlet exception occurs
     * @throws IOException if IO exception occurs
     */
    protected abstract Object createTemplateContext(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;

    /**
     * Resolve the template by the {@code templatePath} and process the template for the request with the
     * templating engine specific {@code context} object.
     * @param request HttpServletRequest instance
     * @param response HttpServletResponse instance
     * @param templatePath template path
     * @param context Templating engine specific <i>Context</i> object which is evaluated by the template
     * @throws ServletException if servlet exception occurs
     * @throws IOException if IO exception occurs
     */
    protected abstract void processTemplate(HttpServletRequest request, HttpServletResponse response,
            String templatePath, Object context) throws ServletException, IOException;

    /**
     * Clear the template cache if available in the subclass. It does nothing by default unless overridden.
     */
    protected void clearTemplateCache() {
    }

    /**
     * Return the template path info, dispatched for the {@code request}.
     * @param request HttpServletRequest instance
     * @return the template path info, dispatched for the {@code request}
     * @throws ServletException if servlet exception occurs
     */
    protected String getTemplatePath(HttpServletRequest request) throws ServletException {
        final String protocol = (String) request.getAttribute(ContainerConstants.DISPATCH_URI_PROTOCOL);
        final String pathInfo = getTemplatePathInfo(request);
        return (protocol != null) ? protocol + pathInfo : pathInfo;
    }

    private String getTemplatePathInfo(HttpServletRequest request) {
        String includeServletPath = (String) request.getAttribute("jakarta.servlet.include.servlet_path");

        if (includeServletPath != null) {
            String includePathInfo = (String) request.getAttribute("jakarta.servlet.include.path_info");
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