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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.templateresource.ITemplateResource;

import java.io.Reader;
import java.io.StringReader;

import static org.onehippo.forge.templating.support.thymeleaf.servlet.resolvers.StringTemplateResolver.*;

public class StringTemplateResource extends ThymeleafTemplateResource {


    private static final Logger log = LoggerFactory.getLogger(StringTemplateResource.class);

    public StringTemplateResource(final IEngineConfiguration configuration, final String template) {
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
        if (template == null) {
            return new StringReader("");
        }
        return new StringReader(template.substring(STRING_PROTOCOL.length(), template.length()));
    }

    @Override public ITemplateResource relative(final String relativeLocation) {
        return null;
    }
}
