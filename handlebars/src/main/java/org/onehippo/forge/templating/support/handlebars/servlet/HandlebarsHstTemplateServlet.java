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
package org.onehippo.forge.templating.support.handlebars.servlet;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.BooleanUtils;
import org.onehippo.forge.templating.support.core.servlet.AbstractHstTemplateServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.cache.ConcurrentMapTemplateCache;
import com.github.jknack.handlebars.cache.TemplateCache;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.ServletContextTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;

/**
 * Handlebars specific Templating Support Servlet for Hippo CMS Delivery tier web application.
 */
public class HandlebarsHstTemplateServlet extends AbstractHstTemplateServlet {

    public static final long serialVersionUID = 1L;

    /**
     * Servlet context init param name for whether or not the template cache should be enabled.
     */
    public static final String PARAM_CACHE_ENABLED = "cache.enabled";

    private static Logger log = LoggerFactory.getLogger(HandlebarsHstTemplateServlet.class);

    /**
     * Handlebars instance.
     */
    private Handlebars handlebars;

    /**
     * Template cache.
     */
    private TemplateCache templateCache;

    @Override
    protected void initializeTemplateEngine(ServletConfig config) throws ServletException {
        handlebars = new Handlebars(createTemplateLoader(config));
        templateCache = createTemplateCache(config);

        if (templateCache != null) {
            handlebars.with(templateCache);
        }
    }

    @Override
    protected Object createTemplateContext(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        return new HippoHandlebarContext(request, response, request.getServletContext());
    }

    @Override
    protected void processTemplate(HttpServletRequest request, HttpServletResponse response, String templatePath,
            Object context) throws ServletException, IOException {
        Template template = handlebars.compile(templatePath);
        template.apply(context, response.getWriter());
    }

    @Override
    protected void clearTemplateCache() {
        if (templateCache != null) {
            templateCache.clear();
        }
    }

    /**
     * Create a template loader. This method is invoked by {@link #initializeTemplateEngine(ServletConfig)} during
     * the initialization phase.
     * <p>
     * By default, this method instantiates an {@link ProtocolBasedDelegatingTemplateLoader} which should be able
     * to handle webfiles or classpath or servlet path based templates by delegating based on the protocol used in
     * the template path.
     * @param config ServletConfig instance
     * @return Handlebars' {@link TemplateLoader} instance.
     * @throws ServletException if servlet exception occurs.
     */
    protected TemplateLoader createTemplateLoader(ServletConfig config) throws ServletException {
        final Map<String, TemplateLoader> prefixTemplateLoadersMap = new LinkedHashMap<>();
        prefixTemplateLoadersMap.put(WEB_FILE_TEMPLATE_PROTOCOL, new WebfileTemplateLoader(""));
        prefixTemplateLoadersMap.put(CLASSPATH_TEMPLATE_PROTOCOL, new ClassPathTemplateLoader("", ""));
        prefixTemplateLoadersMap.put("", new ServletContextTemplateLoader(config.getServletContext(), "/WEB-INF/", ""));
        final ProtocolBasedDelegatingTemplateLoader templateLoader = new ProtocolBasedDelegatingTemplateLoader(
                prefixTemplateLoadersMap);
        return templateLoader;
    }

    /**
     * Create a Handlebars' {@link TemplateCache} instance. This method is invoked by {@link #initializeTemplateEngine(ServletConfig)}
     * during the initialization phase.
     * <p>
     * By default, this method simply instantiates a {@link ConcurrentMapTemplateCache} if {@link #PARAM_CACHE_ENABLED}
     * servlet init parameter is set to "true".
     * @param config ServletConfig instance
     * @return Handlebars' {@link TemplateCache} instance.
     * @throws ServletException if servlet exception occurs.
     */
    protected TemplateCache createTemplateCache(ServletConfig config) throws ServletException {
        if (BooleanUtils.toBoolean(config.getInitParameter(PARAM_CACHE_ENABLED))) {
            return new ConcurrentMapTemplateCache();
        }

        return null;
    }
}