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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ITemplateResolver;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public class ThymeleafHstTemplateServlet extends AbstractHstTemplateServlet {

    private static final Logger log = LoggerFactory.getLogger(ThymeleafHstTemplateServlet.class);

    private TemplateEngine engine;

    @Override protected void initializeTemplateEngine(final ServletConfig config) {
        engine = new TemplateEngine();
        final Set<ITemplateResolver> resolvers = new HashSet<>();
        resolvers.add(new WebfilesTemplateResolver(config));
        resolvers.add(new ClasspathTemplateResolver(config));
        resolvers.add(new ServletTemplateResolver(config));
        engine.setTemplateResolvers(resolvers);
    }


    @Override
    protected Object createTemplateContext(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        return new WebContext(request, response, getServletContext());
    }

    @Override
    protected void processTemplate(final HttpServletRequest request, final HttpServletResponse response, final String templatePath, final Object context) throws ServletException, IOException {
        final String process = engine.process(templatePath, (IContext) context);
        log.debug("process {}", process);
        final PrintWriter writer = response.getWriter();
        writer.write(process);
        writer.flush();
    }
}
