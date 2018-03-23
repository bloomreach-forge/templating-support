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

package org.onehippo.forge.templating.support.thymeleaf.servlet.resources;

import org.hippoecm.hst.container.RequestContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.templateresource.ITemplateResource;

import javax.servlet.ServletContext;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class ServletTemplateResource extends ThymeleafTemplateResource {


    private static final Logger log = LoggerFactory.getLogger(ServletTemplateResource.class);

    public ServletTemplateResource(final IEngineConfiguration configuration, final String template) {
        super(configuration, template);
    }

    @Override public String getDescription() {
        return null;
    }

    @Override public String getBaseName() {
        return null;
    }

    @Override public boolean exists() {
        return false;
    }

    @Override public Reader reader() {
        final ServletContext servletContext = RequestContextProvider.get().getServletContext();
        final InputStream stream = servletContext.getResourceAsStream(template);
        if (stream == null) {
            log.warn("No web resource found for:{}", template);
            return null;
        }
        return new InputStreamReader(stream);
    }

    @Override public ITemplateResource relative(final String relativeLocation) {
        return null;
    }
}
