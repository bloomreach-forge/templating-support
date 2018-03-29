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

package org.onehippo.forge.templating.support.thymeleaf.servlet.resolvers;

import org.onehippo.forge.templating.support.thymeleaf.servlet.resources.ServletTemplateResource;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.TemplateResolution;

import java.util.Map;

public class ServletTemplateResolver extends ThymeleafTemplateResolver {


    @Override public String getName() {
        return "ServletTemplateResolver";
    }

    @Override public Integer getOrder() {
        return 4;
    }

    @Override
    public TemplateResolution resolveTemplate(final IEngineConfiguration configuration, final String ownerTemplate, final String template, final Map<String, Object> templateResolutionAttributes) {
        if (ownerTemplate != null) {
            return new TemplateResolution(new ServletTemplateResource(configuration, createResourceFragmentPath(ownerTemplate, template)), TemplateMode.HTML, CACHED);
        }
        return new TemplateResolution(new ServletTemplateResource(configuration, template), TemplateMode.HTML, CACHED);

    }
}
