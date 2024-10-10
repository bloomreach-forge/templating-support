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
package org.onehippo.forge.templating.support.core.helper;

import org.hippoecm.hst.container.RequestContextProvider;
import org.onehippo.forge.templating.support.core.servlet.TemplateRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.jsp.jstl.core.Config;
import jakarta.servlet.jsp.jstl.fmt.LocalizationContext;
import java.util.ResourceBundle;

/**
 * HST I18n Helper.
 */
public class I18nHelper {

    private static final Logger log = LoggerFactory.getLogger(I18nHelper.class);
    public static final I18nHelper INSTANCE = new I18nHelper();
    public static final String UNDEFINED_KEY = "???";

    private I18nHelper() {
    }

    @SuppressWarnings("UnusedReturnValue")
    public String bundleMessage(final String key) {
        final ServletRequest request = TemplateRequestContext.getRequest();
        if (request == null) {
            log.warn("Request was null");
            return null;
        }
        final LocalizationContext locCtx = (LocalizationContext) Config.get(RequestContextProvider.get().getServletRequest(), Config.FMT_LOCALIZATION_CONTEXT);
        if (locCtx != null) {
            final ResourceBundle rb = locCtx.getResourceBundle();
            if (rb.containsKey(key)) {
                return rb.getString(key);
            }

        }
        return UNDEFINED_KEY + key + UNDEFINED_KEY;
    }


}
