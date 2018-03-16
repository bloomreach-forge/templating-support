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

package org.onehippo.forge.templating.support.thymeleaf.servlet.attributes;

import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.util.HstRequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.WebEngineContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ThymeleafHstIncludeAttribute extends ThymeleafHstAttribute {
    private static final String ATTR_NAME = "include";

    private static final Logger log = LoggerFactory.getLogger(ThymeleafHstIncludeAttribute.class);

    public ThymeleafHstIncludeAttribute(final String dialectPrefix) {
        super(dialectPrefix, ATTR_NAME);
    }

    protected void doProcess(final ITemplateContext context, final IProcessableElementTag tag, final AttributeName attributeName, final String attributeValue, final IElementTagStructureHandler structureHandler) {

        final WebEngineContext ctx = (WebEngineContext) context;
        final HttpServletRequest servletRequest = ctx.getRequest();
        final HttpServletResponse servletResponse = ctx.getResponse();
        final HstResponse hstResponse = HstRequestUtils.getHstResponse(servletRequest, servletResponse);

        if (hstResponse == null) {
            return;
        }

        try {
            // TODO check HST logic
            hstResponse.flushChildContent(attributeValue);
        } catch (IOException e) {
            log.error("", e);
        }


    }
}
