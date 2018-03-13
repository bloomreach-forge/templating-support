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

import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.TemplateResolution;

import java.util.Map;

import static org.onehippo.forge.templating.support.core.servlet.AbstractHstTemplateServlet.*;

public class ClasspathTemplateResolver extends ThymeleafTemplateResolver {
    
    @Override public String getName() {
        return "ClasspathTemplateResolver";
    }

    @Override public Integer getOrder() {
        return 2;
    }

    @Override
    public TemplateResolution resolveTemplate(final IEngineConfiguration configuration, final String ownerTemplate, final String template, final Map<String, Object> templateResolutionAttributes) {
        if (template.startsWith(CLASSPATH_TEMPLATE_PROTOCOL)) {
            return new TemplateResolution(new ClasspathTemplateResource(configuration, template), TemplateMode.HTML, CACHED);
        }
        if (ownerTemplate != null && ownerTemplate.startsWith(CLASSPATH_TEMPLATE_PROTOCOL)) {
            return new TemplateResolution(new ClasspathTemplateResource(configuration, createClassFragmentPath(ownerTemplate, template)), TemplateMode.HTML, CACHED);
        }
        return null;
    }

}
