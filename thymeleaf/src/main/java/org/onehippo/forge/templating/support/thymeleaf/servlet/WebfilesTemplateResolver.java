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
import org.thymeleaf.cache.AlwaysValidCacheEntryValidity;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolution;

import javax.servlet.ServletConfig;
import java.util.Map;

import static org.onehippo.forge.templating.support.core.servlet.AbstractHstTemplateServlet.*;

public class WebfilesTemplateResolver implements ITemplateResolver {


    private static final AlwaysValidCacheEntryValidity CACHED = new AlwaysValidCacheEntryValidity();

    public WebfilesTemplateResolver(final ServletConfig config) {

    }

    @Override public String getName() {
        return null;
    }

    @Override public Integer getOrder() {
        return 1;
    }

    @Override
    public TemplateResolution resolveTemplate(final IEngineConfiguration configuration, final String ownerTemplate, final String template, final Map<String, Object> templateResolutionAttributes) {
        if (template.startsWith(WEB_FILE_TEMPLATE_PROTOCOL) ) {
            return new TemplateResolution(new WebfileTemplateResource(configuration, template), TemplateMode.HTML, CACHED);
        }
        //TODO check owner template
        
        return null;
    }
}
