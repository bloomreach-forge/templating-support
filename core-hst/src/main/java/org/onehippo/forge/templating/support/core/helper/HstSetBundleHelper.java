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

import org.apache.commons.lang.StringUtils;
import org.hippoecm.hst.resourcebundle.CompositeResourceBundle;
import org.hippoecm.hst.resourcebundle.ResourceBundleUtils;
import org.onehippo.forge.templating.support.core.servlet.TemplateRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.jsp.jstl.fmt.LocalizationContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * HST setBundle Helper.
 */
public class HstSetBundleHelper {

    private static final Logger log = LoggerFactory.getLogger(HstSetBundleHelper.class);
    public static final HstSetBundleHelper INSTANCE = new HstSetBundleHelper();


    private HstSetBundleHelper() {
    }

    @SuppressWarnings("UnusedReturnValue")
    public String setBundle(final String baseName, final boolean fallback)  {
        final ServletRequest request = TemplateRequestContext.getRequest();
        if (request == null) {
            log.warn("Request was null");
            return null;
        }
        final Locale locale = request.getLocale();
        final LocalizationContext localizationContext = getContext(locale, baseName, fallback);
        request.setAttribute(HstMessagesHelper.FMT_LOCALIZATION_CONTEXT_REQUEST, localizationContext);
        return null;
    }



    private LocalizationContext getContext(final Locale locale, final String baseName, final boolean fallbackToJavaResourceBundle) {
        final List<ResourceBundle> bundles = new ArrayList<>();
        final String[] bundleIds = StringUtils.split(baseName, " ,\t\f\r\n");
        if (bundleIds != null) {
            for (String bundleId : bundleIds) {
                try {
                    final ResourceBundle bundle = ResourceBundleUtils.getBundle(bundleId, locale, fallbackToJavaResourceBundle);

                    if (bundle != null) {
                        bundles.add(bundle);
                    }
                } catch (Exception e) {
                    log.warn("Failed to get bundle for basename: {}. {}", baseName, e);
                }
            }


        }
        if (bundles.isEmpty()) {
            return new LocalizationContext();
        } else if (bundles.size() == 1) {
            return new LocalizationContext(bundles.get(0));
        } else {
            return new LocalizationContext(new CompositeResourceBundle(bundles.toArray(new ResourceBundle[bundles.size()])));
        }

    }
}
