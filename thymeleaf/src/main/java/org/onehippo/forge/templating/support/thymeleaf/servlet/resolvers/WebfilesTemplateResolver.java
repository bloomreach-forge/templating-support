/*
 * Copyright 2018-2024 Bloomreach B.V. (http://www.bloomreach.com)
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

package org.onehippo.forge.templating.support.thymeleaf.servlet.resolvers;

import org.onehippo.forge.templating.support.thymeleaf.servlet.resources.WebfileTemplateResource;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.TemplateResolution;

import java.util.Map;

import static org.onehippo.forge.templating.support.core.servlet.AbstractHstTemplateServlet.*;

public class WebfilesTemplateResolver extends ThymeleafTemplateResolver {


    @Override public String getName() {
        return "WebfilesTemplateResolver";
    }

    @Override public Integer getOrder() {
        return 1;
    }

    @Override
    public TemplateResolution resolveTemplate(final IEngineConfiguration configuration, final String ownerTemplate, final String template, final Map<String, Object> templateResolutionAttributes) {
        if (template.startsWith(WEB_FILE_TEMPLATE_PROTOCOL)) {
            return new TemplateResolution(new WebfileTemplateResource(configuration, template), TemplateMode.HTML, CACHED);
        }
        if (ownerTemplate != null && ownerTemplate.startsWith(WEB_FILE_TEMPLATE_PROTOCOL)) {
            return new TemplateResolution(new WebfileTemplateResource(configuration, createWebfileFragmentPath(ownerTemplate, template)), TemplateMode.HTML, CACHED);
        }
        return null;
    }


}
