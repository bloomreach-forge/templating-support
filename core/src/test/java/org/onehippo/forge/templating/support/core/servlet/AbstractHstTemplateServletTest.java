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

import org.easymock.EasyMock;
import org.hippoecm.hst.core.container.ComponentManager;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;

import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class AbstractHstTemplateServletTest {

    private MockServletContext servletContext;
    private AbstractHstTemplateServlet servlet1;
    private AbstractHstTemplateServlet servlet2;
    private Map<String, Boolean> servletNameTemplateEngineInitMap = new HashMap<>();

    private HstTemplateServletTemplateCacheInvalidatingEventListener eventListener;
    private ComponentManager componentManager;
    private Map<String, Boolean> servletNameClearTemplateCacheMap = new HashMap<>();

    @Before
    public void setUp() throws Exception {
        servletContext = new MockServletContext();

        servlet1 = new MyExampleHstTemplateServlet();

        MockServletConfig servletConfig1 = new MockServletConfig(servletContext) {
            @Override
            public String getServletName() {
                return "servlet-1";
            }
        };

        servlet1.init(servletConfig1);
        assertTrue(servletNameTemplateEngineInitMap.get("servlet-1"));

        servlet2 = new MyExampleHstTemplateServlet();

        MockServletConfig servletConfig2 = new MockServletConfig(servletContext) {
            @Override
            public String getServletName() {
                return "servlet-2";
            }
        };

        servlet2.init(servletConfig2);
        assertTrue(servletNameTemplateEngineInitMap.get("servlet-2"));

        eventListener = new HstTemplateServletTemplateCacheInvalidatingEventListener();

        componentManager = EasyMock.createNiceMock(ComponentManager.class);
        EasyMock.expect(componentManager.getServletContext()).andReturn(servletContext).anyTimes();
        EasyMock.replay(componentManager);
        eventListener.setComponentManager(componentManager);
    }

    @Test
    public void testContextAttributes() throws Exception {
        @SuppressWarnings("unchecked")
        final Map<String, AbstractHstTemplateServlet> map = (Map<String, AbstractHstTemplateServlet>) servletContext
                .getAttribute(AbstractHstTemplateServlet.CONTEXT_ATTRIBUTE_TEMPLATING_SERVLET_MAP);
        assertNotNull(map);
        assertEquals(2, map.size());
        assertSame(servlet1, map.get("servlet-1"));
        assertSame(servlet2, map.get("servlet-2"));
    }

    @Test
    public void testGetTemplatePath() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();

        // when the request comes directly to the servlet.
        request.setPathInfo("/a/b/c.html");
        assertEquals("/a/b/c.html", servlet1.getTemplatePath(request));

        // when the request comes by dispatching to the servlet through include servlet path.
        request.setAttribute("javax.servlet.include.servlet_path", "/d/e/f.html");
        assertEquals("/d/e/f.html", servlet1.getTemplatePath(request));

        // when the request comes by dispatching to the servlet through include path info.
        request.setAttribute("javax.servlet.include.path_info", "/g/h/i.html");
        assertEquals("/g/h/i.html", servlet1.getTemplatePath(request));
    }


    @Test
    public void testProcessTemplate() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setPathInfo("/a/b/c.html");
        servlet1.doGet(request, response);
        assertEquals("templatePath: /a/b/c.html, context: context object", response.getContentAsString());
    }

    @Test
    public void testHstTemplateServletTemplateCacheInvalidatingEventListener() throws Exception {
        Event event = EasyMock.createNiceMock(Event.class);
        EasyMock.replay(event);
        int eventItCount = 0;

        EventIterator eventIt = EasyMock.createNiceMock(EventIterator.class);
        EasyMock.expect(eventIt.hasNext()).andReturn(eventItCount++ == 0).anyTimes();
        EasyMock.expect(eventIt.nextEvent()).andReturn(event).anyTimes();
        EasyMock.replay(eventIt);

        eventListener.onEvent(eventIt);

        assertEquals(2, servletNameClearTemplateCacheMap.size());
        assertTrue(servletNameClearTemplateCacheMap.get("servlet-1"));
        assertTrue(servletNameClearTemplateCacheMap.get("servlet-2"));
    }

    private class MyExampleHstTemplateServlet extends AbstractHstTemplateServlet {

        private static final long serialVersionUID = 1L;

        @Override
        protected void initializeTemplateEngine(ServletConfig config) throws ServletException {
            servletNameTemplateEngineInitMap.put(config.getServletName(), Boolean.TRUE);
        }

        @Override
        protected Object createTemplateContext(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            return "context object";
        }

        @Override
        protected void processTemplate(HttpServletRequest request, HttpServletResponse response,
                String templatePath, Object context) throws ServletException, IOException {
            response.getWriter().write("templatePath: " + templatePath + ", context: " + context);
        }

        @Override
        protected void clearTemplateCache() {
            servletNameClearTemplateCacheMap.put(getServletConfig().getServletName(), Boolean.TRUE);
        }
    }
}
