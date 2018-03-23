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

import org.onehippo.forge.templating.support.core.servlet.AbstractHstTemplateServlet;
import org.onehippo.forge.templating.support.thymeleaf.servlet.resolvers.ClasspathTemplateResolver;
import org.onehippo.forge.templating.support.thymeleaf.servlet.resolvers.ServletTemplateResolver;
import org.onehippo.forge.templating.support.thymeleaf.servlet.resolvers.WebfilesTemplateResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;
import org.thymeleaf.templateresolver.ITemplateResolver;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ThymeleafHstTemplateServlet extends AbstractHstTemplateServlet {

    private static final Logger log = LoggerFactory.getLogger(ThymeleafHstTemplateServlet.class);

    private TemplateEngine engine;
    private String webResourcePrefix;

    @Override
    protected void initializeTemplateEngine(final ServletConfig config) {
        engine = new TemplateEngine();
        final String resourcePrefix = config.getInitParameter("webResourcePrefix");
        if (resourcePrefix != null) {
            webResourcePrefix = resourcePrefix;
        }
        final Set<ITemplateResolver> resolvers = new HashSet<>();
        resolvers.add(new WebfilesTemplateResolver());
        resolvers.add(new ClasspathTemplateResolver());
        resolvers.add(new ServletTemplateResolver());
        engine.setTemplateResolvers(resolvers);
        engine.addDialect(new ThymeleafHstDialect());
        engine.setLinkBuilder(new ThymeleafLinkBuilder());
    }


    @Override
    protected Object createTemplateContext(final HttpServletRequest request, final HttpServletResponse response) {
        return new HippoThymeleafContext(request, response, getServletContext());
    }

    @Override
    protected void processTemplate(final HttpServletRequest request, final HttpServletResponse response, final String templatePath, final Object context) throws IOException {
        log.debug("Processing template: {}", templatePath);
        engine.process(withPrefix(templatePath), (IContext) context, response.getWriter());
    }

    private String withPrefix(final String templatePath) {
        if (webResourcePrefix == null) {
            return templatePath;
        }
        return webResourcePrefix + templatePath;
    }

    @Override
    protected void clearTemplateCache() {
        if (engine != null) {
            engine.clearTemplateCache();
        }
    }
}
