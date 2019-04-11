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

package org.onehippo.forge.templating.support.pebble.servlet;

import org.onehippo.forge.templating.support.core.servlet.AbstractHstTemplateServlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PebbleHstTemplateServlet extends AbstractHstTemplateServlet {


    @Override
    protected void initializeTemplateEngine(final ServletConfig config) throws ServletException {
        
    }

    @Override
    protected Object createTemplateContext(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        return null;
    }

    @Override
    protected void processTemplate(final HttpServletRequest request, final HttpServletResponse response, final String templatePath, final Object context) throws ServletException, IOException {

    }
}
