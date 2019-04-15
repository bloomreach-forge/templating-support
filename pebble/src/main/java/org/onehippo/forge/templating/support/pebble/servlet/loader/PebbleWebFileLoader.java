/*
 * Copyright 2019 Hippo B.V. (http://www.onehippo.com)
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

package org.onehippo.forge.templating.support.pebble.servlet.loader;

import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import javax.jcr.RepositoryException;

import org.onehippo.forge.templating.support.core.util.JcrTemplateSourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mitchellbosecke.pebble.loader.Loader;


public class PebbleWebFileLoader implements Loader<String> {
    private static final Logger log = LoggerFactory.getLogger(PebbleWebFileLoader.class);
    private String charSet;

    public PebbleWebFileLoader() {
        this.charSet = StandardCharsets.UTF_8.name();
    }

    @Override
    public Reader getReader(final String template) {
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

    @Override
    public void setCharset(final String charset) {
        this.charSet = charset;
    }

    @Override
    public void setPrefix(final String prefix) {

    }

    @Override
    public void setSuffix(final String suffix) {

    }

    @Override
    public String resolveRelativePath(final String relativePath, final String anchorPath) {
        // TODO implement
        return relativePath;
    }

    @Override
    public String createCacheKey(final String templateName) {
        return templateName;
    }
}
