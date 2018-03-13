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

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.util.HstRequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.AbstractContext;
import org.thymeleaf.context.IWebContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.hippoecm.hst.tag.DefineObjectsTag.*;

public class HippoThymeleafContext extends AbstractContext implements IWebContext {


    private static final Logger log = LoggerFactory.getLogger(HippoThymeleafContext.class);
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final ServletContext servletContext;



    public HippoThymeleafContext(final HttpServletRequest request, final HttpServletResponse response, final ServletContext servletContext) {
        this.request = request;
        this.response = response;
        this.servletContext = servletContext;
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



}
