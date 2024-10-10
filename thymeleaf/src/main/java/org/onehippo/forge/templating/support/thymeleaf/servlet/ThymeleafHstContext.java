/*
 * Copyright 2018-2024 Bloomreach B.V. (http://www.bloomreach.com)
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

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.util.HstRequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.AbstractContext;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import static org.hippoecm.hst.tag.DefineObjectsTag.*;

public class ThymeleafHstContext extends AbstractContext implements IWebContext {


    private static final Logger log = LoggerFactory.getLogger(ThymeleafHstContext.class);
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final ServletContext servletContext;
    private final IWebExchange exchange;


    public ThymeleafHstContext(final HttpServletRequest request, final HttpServletResponse response, final ServletContext servletContext) {
        this.request = request;
        this.response = response;
        this.servletContext = servletContext;
        this.exchange = JakartaServletWebApplication.buildApplication(servletContext).buildExchange(request,response);
        defineHstObjects();
    }

    private void defineHstObjects() {
        final HstRequest hstRequest = HstRequestUtils.getHstRequest(request);
        final HstResponse hstResponse = HstRequestUtils.getHstResponse(request, response);
        setVariable(HST_REQUEST_CONTEXT_ATTR_NAME, RequestContextProvider.get());
        setVariable(HST_REQUEST_ATTR_NAME, hstRequest);
        setVariable(HST_RESPONSE_ATTR_NAME, hstResponse);
        if (hstResponse != null) {
            try {
                setVariable(HST_RESPONSE_CHILD_CONTENT_NAMES_ATTR_NAME, hstResponse.getChildContentNames());
            } catch (RuntimeException e) {
                log.debug("hstResponse of class '{}'cannot return child content names. This is not a problem. Child " + "content names are skipped.", hstResponse.getClass().getName());
            }
        }
    }


    public HttpServletRequest getRequest() {
        return this.request;
    }

    public HttpSession getSession() {
        return this.request.getSession(false);
    }

    public HttpServletResponse getResponse() {
        return this.response;
    }

    public ServletContext getServletContext() {
        return this.servletContext;
    }

    @Override
    public IWebExchange getExchange() {
        return this.exchange;
    }
}
