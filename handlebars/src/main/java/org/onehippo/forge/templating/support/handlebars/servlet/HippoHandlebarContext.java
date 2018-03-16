package org.onehippo.forge.templating.support.handlebars.servlet;

import org.hippoecm.hst.container.*;
import org.hippoecm.hst.core.component.*;
import org.hippoecm.hst.util.*;
import org.slf4j.*;

import javax.servlet.*;
import javax.servlet.http.*;

public class HippoHandlebarContext {

    private static final Logger log = LoggerFactory.getLogger(HippoHandlebarContext.class);
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final ServletContext servletContext;
    private final HstRequest hstRequest;
    private final HstResponse hstResponse;


    public HippoHandlebarContext(final HttpServletRequest request, final HttpServletResponse response, final ServletContext servletContext) {
        this.request = request;
        this.response = response;
        this.servletContext = servletContext;
        this.hstRequest = HstRequestUtils.getHstRequest(request);
        this.hstResponse = HstRequestUtils.getHstResponse(request, response);
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

    public HstRequest getHstRequest() { return hstRequest; }

    public HstResponse getHstResponse() { return hstResponse; }

}
