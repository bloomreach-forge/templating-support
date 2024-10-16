/*
 * Copyright 2018-2024 Bloomreach B.V. (http://www.bloomreach.com)
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

import java.io.IOException;
import java.io.StringWriter;

import org.hippoecm.hst.core.component.HstResponse;
import org.onehippo.forge.templating.support.core.servlet.TemplateRequestContext;

/**
 * HST Link Creation Helper.
 */
public class HstIncludeHelper {

    public static final HstIncludeHelper INSTANCE = new HstIncludeHelper();

    private HstIncludeHelper() {
    }

    public String includeChild(String ref) throws IOException {
        final HstResponse hstResponse = TemplateRequestContext.getHstResponse();
        StringWriter writer = new StringWriter(512);
        hstResponse.flushChildContent(ref, writer);
        return writer.toString();
    }
}
