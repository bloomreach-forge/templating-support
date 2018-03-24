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

import org.hippoecm.hst.core.linking.HstLink;
import org.hippoecm.hst.core.linking.HstLinkCreator;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.util.HstRequestUtils;
import org.onehippo.forge.templating.support.core.helper.HstWebfilesHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.context.WebEngineContext;
import org.thymeleaf.engine.TemplateData;
import org.thymeleaf.linkbuilder.ILinkBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static org.onehippo.forge.templating.support.core.servlet.AbstractHstTemplateServlet.*;

public class ThymeleafLinkBuilder implements ILinkBuilder {

    private static final Logger log = LoggerFactory.getLogger(ThymeleafLinkBuilder.class);


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
        if (template != null) {
            if (template.startsWith(WEB_FILE_TEMPLATE_PROTOCOL)) {
                return HstWebfilesHelper.INSTANCE.webfileByPath(createAbsolutePath(stack, base, WEB_FILE_TEMPLATE_PROTOCOL), false);
                //return ThymeleafUtils.createWebfileLink(ctx, createAbsolutePath(stack, base, WEB_FILE_TEMPLATE_PROTOCOL));
            } else if (template.startsWith(CLASSPATH_TEMPLATE_PROTOCOL)) {

                return createLink(ctx, createAbsolutePath(stack, base, CLASSPATH_TEMPLATE_PROTOCOL));
            }
        }
        return createLink(ctx, createAbsolutePath(stack, base, ""));

    }

    private String createLink(final WebEngineContext ctx, final String path) {
        final HttpServletRequest servletRequest = ctx.getRequest();
        final HstRequestContext reqContext = HstRequestUtils.getHstRequestContext(servletRequest);
        final HstLinkCreator creator = reqContext.getHstLinkCreator();
        final HstLink link = creator.create(path, reqContext.getResolvedMount().getMount());
        if (link != null) {
            return link.getPath();
        }

        return path;
    }


    private String createAbsolutePath(final List<TemplateData> stack, final String base, final String protocol) {
        if (base.startsWith("/")) {
            return base;
        }
        final boolean hasProtocol = protocol.length() > 0;
        String path = "";
        for (TemplateData templateData : stack) {
            final String template = templateData.getTemplate();
            if (hasProtocol && template.startsWith(protocol)) {
                path = template.substring(protocol.length(), template.lastIndexOf('/'));;
            }
            else if (template.startsWith("/")) {
                path = template.substring(0, template.lastIndexOf('/'));
            } else {
                if (template.indexOf('/') > 0) {
                    path = path + '/' + template.substring(0, template.lastIndexOf('/'));
                } else {
                    path = path.substring(0, path.lastIndexOf('/'));
                }
            }
        }
        return path + '/' + base;
    }

}
