/*
 * Copyright 2018-2019 Bloomreach B.V. (http://www.bloomreach.com)
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.templateresource.ITemplateResource;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static org.onehippo.forge.templating.support.core.servlet.AbstractHstTemplateServlet.*;

public class ClasspathTemplateResource extends ThymeleafTemplateResource {


    private static final Logger log = LoggerFactory.getLogger(ClasspathTemplateResource.class);
    private static final int SUBSTRING_SIZE = CLASSPATH_TEMPLATE_PROTOCOL.length();

    public ClasspathTemplateResource(final IEngineConfiguration configuration, final String template) {
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
        final String classPath = template.substring(SUBSTRING_SIZE);

        final InputStream stream = getClass().getResourceAsStream(classPath);
        if (stream == null) {
            log.warn("No classpath resource found for:{} ({})", template, classPath);
            return null;
        }
        return new InputStreamReader(stream);
    }

    @Override public ITemplateResource relative(final String relativeLocation) {
        return null;
    }
}
