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

import org.onehippo.forge.templating.support.core.util.JcrTemplateSourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.templateresource.ITemplateResource;

import javax.jcr.RepositoryException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class ClasspathTemplateResource implements ITemplateResource {


    private static final Logger log = LoggerFactory.getLogger(ClasspathTemplateResource.class);

    private final IEngineConfiguration configuration;
    private final String template;

    public ClasspathTemplateResource(final IEngineConfiguration configuration, final String template) {
        this.configuration = configuration;
        this.template = template;
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

    @Override public Reader reader() throws IOException {
        final String jcrTemplatePath = JcrTemplateSourceUtils.getWebFileJcrPath(template);

        try {
            final String sourceContent = JcrTemplateSourceUtils.getTemplateSourceContent(jcrTemplatePath);
            if (sourceContent == null) {
                log.warn("Empty content for: {}", template);
                return new StringReader("");
            }
            return new StringReader(sourceContent);
        } catch (RepositoryException e) {
            log.warn("Cannot load template for {}.", template);
        }


        return null;
    }

    @Override public ITemplateResource relative(final String relativeLocation) {
        return null;
    }
}
