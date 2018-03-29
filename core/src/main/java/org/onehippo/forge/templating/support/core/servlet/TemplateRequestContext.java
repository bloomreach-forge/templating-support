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
package org.onehippo.forge.templating.support.core.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.util.HstRequestUtils;

/**
 * Template request context.
 */
public class TemplateRequestContext {

    private static ThreadLocal<TemplateRequestContext> tlTemplateRequestContext = new ThreadLocal<>();

    public static HttpServletRequest getRequest() {
        final TemplateRequestContext context = tlTemplateRequestContext.get();

        if (context != null) {
            return context.templateRequest;
        }

        return null;
    }

    public static HstRequest getHstRequest() {
        final HttpServletRequest request = getRequest();

        if (request != null) {
            return HstRequestUtils.getHstRequest(request);
        }

        return null;
    }

    public static HttpServletResponse getResponse() {
        final TemplateRequestContext context = tlTemplateRequestContext.get();

        if (context != null) {
            return context.templateResponse;
        }

        return null;
    }

    public static HstResponse getHstResponse() {
        final HttpServletRequest request = getRequest();
        final HttpServletResponse response = getResponse();

        if (request != null) {
            return HstRequestUtils.getHstResponse(request, response);
        }

        return null;
    }

    static void reset(final HttpServletRequest templateRequest, final HttpServletResponse templateResponse) {
        tlTemplateRequestContext.set(new TemplateRequestContext(templateRequest, templateResponse));
    }

    static void clear() {
        tlTemplateRequestContext.remove();
    }

    private final HttpServletRequest templateRequest;
    private final HttpServletResponse templateResponse;

    private TemplateRequestContext(final HttpServletRequest templateRequest,
            final HttpServletResponse templateResponse) {
        this.templateRequest = templateRequest;
        this.templateResponse = templateResponse;
    }
}
