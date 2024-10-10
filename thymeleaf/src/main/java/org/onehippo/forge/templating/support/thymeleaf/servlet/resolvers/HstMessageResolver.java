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

package org.onehippo.forge.templating.support.thymeleaf.servlet.resolvers;

import org.hippoecm.hst.container.RequestContextProvider;
import org.onehippo.forge.templating.support.core.helper.HstMessagesHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.WebEngineContext;
import org.thymeleaf.messageresolver.AbstractMessageResolver;
import org.thymeleaf.messageresolver.StandardMessageResolver;
import org.thymeleaf.web.IWebRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.jsp.jstl.core.Config;
import jakarta.servlet.jsp.jstl.fmt.LocalizationContext;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class HstMessageResolver extends AbstractMessageResolver {

    private static final Logger log = LoggerFactory.getLogger(HstMessageResolver.class);
    private static final Object[] EMPTY_MESSAGE_PARAMETERS = new Object[0];
    private final StandardMessageResolver standardMessageResolver;

    public HstMessageResolver() {
        this.standardMessageResolver = new StandardMessageResolver();
    }

    @Override
    public String resolveMessage(final ITemplateContext context, final Class<?> origin, final String key, final Object[] messageParameters) {
        final WebEngineContext ctx = (WebEngineContext) context;
        final HttpServletRequest request = (HttpServletRequest)ctx.getVariable("hstRequest");
        final LocalizationContext locCtx = (LocalizationContext) Config.get(RequestContextProvider.get().getServletRequest(), Config.FMT_LOCALIZATION_CONTEXT);
        String message = getMessage(key, locCtx);
        if (message != null) {
            return formatMessage(ctx.getLocale(), message, messageParameters);
        }
        final LocalizationContext localizationContext = (LocalizationContext) request.getAttribute(HstMessagesHelper.FMT_LOCALIZATION_CONTEXT_REQUEST);
        message = getMessage(key, localizationContext);
        if (message != null) {
            return formatMessage(ctx.getLocale(), message, messageParameters);
        }
        return standardMessageResolver.resolveMessage(context, origin, key, messageParameters);
    }

    private String getMessage(final String key, final LocalizationContext localizationContext) {
        if (localizationContext != null) {
            final ResourceBundle resourceBundle = localizationContext.getResourceBundle();
            if (resourceBundle != null && resourceBundle.containsKey(key)) {
                return resourceBundle.getString(key);
            }
        }
        return null;
    }

    
    @Override
    public String createAbsentMessageRepresentation(final ITemplateContext context, final Class<?> origin, final String key, final Object[] messageParameters) {
        return standardMessageResolver.createAbsentMessageRepresentation(context, origin, key, messageParameters);
    }




    private static String formatMessage(final Locale locale, final String message, final Object[] messageParameters) {
        if (message == null) {
            return null;
        }
        if (!isFormatCandidate(message)) { // trying to avoid creating MessageFormat if not needed
            return message;
        }
        final MessageFormat messageFormat = new MessageFormat(message, locale);
        return messageFormat.format((messageParameters != null ? messageParameters : EMPTY_MESSAGE_PARAMETERS));
    }


    /*
     * This will allow us determine whether a message might actually contain parameter placeholders.
     */
    private static boolean isFormatCandidate(final String message) {
        char c;
        int n = message.length();
        while (n-- != 0) {
            c = message.charAt(n);
            if (c == '}' || c == '\'') {
                return true;
            }
        }
        return false;
    }

}
