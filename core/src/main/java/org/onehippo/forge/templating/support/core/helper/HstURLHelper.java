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
package org.onehippo.forge.templating.support.core.helper;

import org.hippoecm.hst.core.component.HstURL;
import org.onehippo.forge.templating.support.core.servlet.TemplateRequestContext;

/**
 * HST URL Helper.
 */
public class HstURLHelper {

    public static final HstURLHelper INSTANCE = new HstURLHelper();

    private HstURLHelper() {
    }

    public String renderURL() {
        final HstURL url = TemplateRequestContext.getHstResponse().createRenderURL();
        return url.toString();
    }

    public String actionURL() {
        final HstURL url = TemplateRequestContext.getHstResponse().createActionURL();
        return url.toString();
    }

    public String resourceURL(String resourceID) {
        final HstURL url = TemplateRequestContext.getHstResponse().createResourceURL(resourceID);
        return url.toString();
    }

    public String componentRenderingURL() {
        final HstURL url = TemplateRequestContext.getHstResponse().createComponentRenderingURL();
        return url.toString();
    }
}
