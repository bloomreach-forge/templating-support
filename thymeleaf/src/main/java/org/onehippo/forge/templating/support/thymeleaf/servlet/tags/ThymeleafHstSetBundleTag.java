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

package org.onehippo.forge.templating.support.thymeleaf.servlet.tags;

import org.apache.commons.lang.StringUtils;
import org.hippoecm.hst.resourcebundle.CompositeResourceBundle;
import org.hippoecm.hst.resourcebundle.ResourceBundleUtils;
import org.onehippo.forge.templating.support.core.helper.HstMessagesHelper;
import org.onehippo.forge.templating.support.core.servlet.TemplateRequestContext;
import org.onehippo.forge.templating.support.thymeleaf.servlet.attributes.ThymeleafHstHtmlAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IModel;
import org.thymeleaf.processor.element.IElementModelStructureHandler;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ThymeleafHstSetBundleTag extends BaseModelProcessor {
    private static final String TAG_NAME = "setBundle";

    private static final Logger log = LoggerFactory.getLogger(ThymeleafHstHtmlAttribute.class);

    public ThymeleafHstSetBundleTag(final String dialectPrefix) {
        super(dialectPrefix, TAG_NAME);
    }

    @Override
    protected void doProcess(final ITemplateContext context, final IModel model, final IElementModelStructureHandler structureHandler) {
        final String baseName = getAttributeExpression(context, model, "hst:basename");
        final boolean fallback = getBooleanOrExpression(context, model, "hst:fallbackToJavaResourceBundle");
        final Locale locale = context.getLocale();
        final LocalizationContext localizationContext = getContext(locale, baseName, fallback);

        final ServletRequest request = TemplateRequestContext.getRequest();
        if (request == null) {
            log.warn("Request was null");
            return;
        }
        request.setAttribute(HstMessagesHelper.FMT_LOCALIZATION_CONTEXT_REQUEST, localizationContext);
        // don't render tag
        model.reset();
    }

    public LocalizationContext getContext(final Locale locale, final String baseName, final boolean fallbackToJavaResourceBundle) {
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
