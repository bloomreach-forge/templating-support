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

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hippoecm.hst.core.component.HstURL;
import org.onehippo.forge.templating.support.core.servlet.TemplateRequestContext;
import org.onehippo.forge.templating.support.core.util.QueryStringUtils;

/**
 * HST URL Helper.
 */
public class HstURLHelper {

    public static final HstURLHelper INSTANCE = new HstURLHelper();

    private HstURLHelper() {
    }

    public String renderURL(String paramsInQueryStringFormat) {
        final HstURL url = TemplateRequestContext.getHstResponse().createRenderURL();
        setHstURLParameters(url, paramsInQueryStringFormat);
        return url.toString();
    }

    public String actionURL(String paramsInQueryStringFormat) {
        final HstURL url = TemplateRequestContext.getHstResponse().createActionURL();
        setHstURLParameters(url, paramsInQueryStringFormat);
        return url.toString();
    }

    public String resourceURL(String resourceID, String paramsInQueryStringFormat) {
        final HstURL url = TemplateRequestContext.getHstResponse().createResourceURL(resourceID);
        setHstURLParameters(url, paramsInQueryStringFormat);
        return url.toString();
    }

    public String componentRenderingURL(String paramsInQueryStringFormat) {
        final HstURL url = TemplateRequestContext.getHstResponse().createComponentRenderingURL();
        setHstURLParameters(url, paramsInQueryStringFormat);
        return url.toString();
    }

    private void setHstURLParameters(final HstURL url, final String paramsInQueryStringFormat) {
        if (StringUtils.isNotBlank(paramsInQueryStringFormat)) {
            try {
                Map<String, String[]> paramsMap = QueryStringUtils.parse(paramsInQueryStringFormat, "UTF-8");
                url.setParameters(paramsMap);
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid parameters.");
            }
        }
    }
}
