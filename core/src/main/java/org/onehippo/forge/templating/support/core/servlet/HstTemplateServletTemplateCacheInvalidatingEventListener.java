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

import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.observation.EventIterator;

import org.hippoecm.hst.core.container.ComponentManager;
import org.hippoecm.hst.core.container.ComponentManagerAware;
import org.hippoecm.hst.core.jcr.GenericEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generic JCR Observation Event Listener implementation, listening on HST template configuration changes
 * and webfiles changes, which looks up all the servlet instances extending {@link AbstractHstTemplateServlet}
 * from the servlet context attribute and invokes {@link AbstractHstTemplateServlet#clearTemplateCache()} operations
 * to give the templating engine specific servlet implementations a chance to clear their own template caches if
 * needed.
 */
public class HstTemplateServletTemplateCacheInvalidatingEventListener extends GenericEventListener
        implements ComponentManagerAware {

    private static Logger log = LoggerFactory.getLogger(HstTemplateServletTemplateCacheInvalidatingEventListener.class);

    /**
     * HST ComponentManager.
     */
    private ComponentManager componentManager;

    @Override
    public void setComponentManager(ComponentManager componentManager) {
        this.componentManager = componentManager;
    }

    @Override
    public void onEvent(EventIterator events) {
        boolean ignorable = true;

        while (events.hasNext()) {
            try {
                if (!eventIgnorable(events.nextEvent())) {
                    ignorable = false;
                    break;
                }
            } catch (RepositoryException e) {
                log.error("Error processing event.");
            }
        }

        if (ignorable) {
            return;
        }

        Map<String, AbstractHstTemplateServlet> templatingServletMap = (Map<String, AbstractHstTemplateServlet>) componentManager
                .getServletContext().getAttribute(AbstractHstTemplateServlet.CONTEXT_ATTRIBUTE_TEMPLATING_SERVLET_MAP);

        if (templatingServletMap != null) {
            synchronized (templatingServletMap) {
                for (AbstractHstTemplateServlet servlet : templatingServletMap.values()) {
                    servlet.clearTemplateCache();
                }
            }
        }
    }
}
