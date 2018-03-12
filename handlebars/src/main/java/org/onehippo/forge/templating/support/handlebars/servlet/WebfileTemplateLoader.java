/*
 * Copyright 2018 Hippo B.V. (http://www.onehippo.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *         http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onehippo.forge.templating.support.handlebars.servlet;

import java.io.IOException;

import javax.jcr.RepositoryException;

import org.onehippo.forge.templating.support.core.util.JcrTemplateSourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jknack.handlebars.io.AbstractTemplateLoader;
import com.github.jknack.handlebars.io.StringTemplateSource;
import com.github.jknack.handlebars.io.TemplateSource;

/**
 * Hippo CMS' webfile template loader implementation.
 */
public class WebfileTemplateLoader extends AbstractTemplateLoader {

    private static Logger log = LoggerFactory.getLogger(WebfileTemplateLoader.class);

    /**
     * Suffix that should be appended to the given template path when resolving the webfile's JCR path.
     */
    private final String suffix;

    /**
     * Construct WebfileTemplateLoader with the suffix.
     * @param suffix Suffix that should be appended to the given template path when resolving the webfile's JCR path
     */
    public WebfileTemplateLoader(final String suffix) {
        this.suffix = suffix;
    }

    @Override
    public TemplateSource sourceAt(String location) throws IOException {
        final String templatePath = (suffix != null) ? location + suffix : location;
        final String jcrTemplatePath = JcrTemplateSourceUtils.getWebFileJcrPath(templatePath);

        try {
            final String sourceContent = JcrTemplateSourceUtils.getTemplateSourceContent(jcrTemplatePath);
            return new StringTemplateSource(templatePath, sourceContent);
        } catch (RepositoryException e) {
            log.warn("Cannot load template source for {}.", location);
        }

        return null;
    }

}