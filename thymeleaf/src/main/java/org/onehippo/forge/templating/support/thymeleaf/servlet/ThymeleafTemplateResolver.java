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

import org.thymeleaf.cache.AlwaysValidCacheEntryValidity;
import org.thymeleaf.templateresolver.ITemplateResolver;

import static org.onehippo.forge.templating.support.core.servlet.AbstractHstTemplateServlet.*;

public abstract class ThymeleafTemplateResolver implements ITemplateResolver {

    protected static final AlwaysValidCacheEntryValidity CACHED = new AlwaysValidCacheEntryValidity();

    protected String createClassFragmentPath(final String ownerTemplate, final String template) {
        return createFragmentPath(ownerTemplate, template, CLASSPATH_TEMPLATE_PROTOCOL);
    }

    protected String createResourceFragmentPath(final String ownerTemplate, final String template) {
        return createFragmentPath(ownerTemplate, template, "");
    }

    protected String createWebfileFragmentPath(final String ownerTemplate, final String template) {
        return createFragmentPath(ownerTemplate, template, WEB_FILE_TEMPLATE_PROTOCOL);
    }

    private String createFragmentPath(final String ownerTemplate, final String template, final String protocol) {
        if (template.startsWith("/")) {
            final String path = protocol + template;
            return createExtension(path);
        }
        final String webPath = ownerTemplate.substring(0, ownerTemplate.lastIndexOf('/')) + '/' + template;
        return createExtension(webPath);
    }

    private String createExtension(final String webPath) {
        if (webPath.endsWith(".html")) {
            return webPath;
        }
        return webPath + ".html";
    }

}
