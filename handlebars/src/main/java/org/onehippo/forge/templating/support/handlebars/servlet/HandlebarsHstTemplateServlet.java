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
package org.onehippo.forge.templating.support.handlebars.servlet;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.cache.ConcurrentMapTemplateCache;
import com.github.jknack.handlebars.cache.TemplateCache;
import com.github.jknack.handlebars.context.JavaBeanValueResolver;
import com.github.jknack.handlebars.context.MapValueResolver;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.ServletContextTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.hippoecm.hst.util.NodeUtils;
import org.onehippo.forge.templating.support.core.helper.*;
import org.onehippo.forge.templating.support.core.servlet.AbstractHstTemplateServlet;
import org.onehippo.forge.templating.support.handlebars.util.HandlebarsHelperRegistrationUtils;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Handlebars specific Templating Support Servlet for Hippo CMS Delivery tier web application.
 */
public class HandlebarsHstTemplateServlet extends AbstractHstTemplateServlet {

    public static final long serialVersionUID = 1L;

    /**
     * Servlet context init param name for whether or not the template cache should be enabled.
     */
    public static final String PARAM_CACHE_ENABLED = "cache.enabled";

    /**
     * Servlet context init param name for the prefix of HST related helpers.
     */
    public static final String PARAM_HST_HELPERS_PREFIX = "hst.helpers.prefix";

    /**
     * Default HST related helpers prefix.
     */
    public static final String DEFAULT_HST_HELPERS_PREFIX = "hst:";

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

        registerHelpers(config, handlebars);
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected Object createTemplateContext(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        final HandlebarsContextModelMap contetModelMap =
                new HandlebarsContextModelMap(
                        new DelegatingTransformer(
                                new RequestAttributeMapTransformer(request),
                                new HstDefineObjectsMapTransformer(request, response)
                                )
                        );

        final Context context =
                Context.newBuilder(contetModelMap)
                .resolver(MapValueResolver.INSTANCE, JavaBeanValueResolver.INSTANCE)
                .build();

        return context;
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

    /**
     * Register default helpers.
     * @param config ServletConfig instance
     * @param handlebars Handlebars instance
     */
    protected void registerHelpers(ServletConfig config, Handlebars handlebars) {
        final String hstHelpersPrefix = StringUtils.defaultString(
                StringUtils.trim(config.getInitParameter(PARAM_HST_HELPERS_PREFIX)),
                DEFAULT_HST_HELPERS_PREFIX);
        HandlebarsHelperRegistrationUtils.registerHelpers(handlebars, hstHelpersPrefix, HstLinkHelper.INSTANCE);
        HandlebarsHelperRegistrationUtils.registerHelpers(handlebars, hstHelpersPrefix, HstHtmlHelper.INSTANCE);
        HandlebarsHelperRegistrationUtils.registerHelpers(handlebars, hstHelpersPrefix, HstURLHelper.INSTANCE);
        HandlebarsHelperRegistrationUtils.registerHelpers(handlebars, hstHelpersPrefix, HstWebfilesHelper.INSTANCE);
        HandlebarsHelperRegistrationUtils.registerHelpers(handlebars, hstHelpersPrefix, HstIncludeHelper.INSTANCE);
        HandlebarsHelperRegistrationUtils.registerHelpers(handlebars, hstHelpersPrefix, HstHeadContributionHelper.INSTANCE);
        HandlebarsHelperRegistrationUtils.registerHelpers(handlebars, hstHelpersPrefix, HstMessagesHelper.INSTANCE);
        HandlebarsHelperRegistrationUtils.registerHelpers(handlebars, hstHelpersPrefix, CmsEditLinkHelper.INSTANCE);
        HandlebarsHelperRegistrationUtils.registerHelpers(handlebars, hstHelpersPrefix, HstSetBundleHelper.INSTANCE);
        HandlebarsHelperRegistrationUtils.registerHelpers(handlebars, hstHelpersPrefix, I18nHelper.INSTANCE);
        HandlebarsHelperRegistrationUtils.registerHelpers(handlebars, hstHelpersPrefix, PropertyUtils.class);
        HandlebarsHelperRegistrationUtils.registerHelpers(handlebars, hstHelpersPrefix, NodeUtils.class);
    }
}