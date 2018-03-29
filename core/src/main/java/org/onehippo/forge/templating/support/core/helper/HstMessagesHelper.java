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

import org.apache.commons.lang.StringUtils;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.utils.MessageUtils;

import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * HST messages Helper.
 */
public class HstMessagesHelper {

    public static final HstMessagesHelper INSTANCE = new HstMessagesHelper();
    public static final String FMT_LOCALIZATION_CONTEXT_REQUEST = Config.FMT_LOCALIZATION_CONTEXT + ".request";

    private HstMessagesHelper() {
    }

    public String replaceByBundle(String text, ResourceBundle bundle) throws IOException {
        if (bundle == null) {
            LocalizationContext locCtx = (LocalizationContext) Config
                    .get(RequestContextProvider.get().getServletRequest(), Config.FMT_LOCALIZATION_CONTEXT);

            if (locCtx != null) {
                bundle = locCtx.getResourceBundle();
            }
        }
        if (bundle == null) {
            return null;
        }
        return MessageUtils.replaceMessagesByBundle(bundle, text);
    }

    public String replaceByBundleName(String text, String basename) throws IOException {
        if (StringUtils.isBlank(basename)) {
            return replaceByBundle(text, null);
        }

        return MessageUtils.replaceMessages(basename, text);
    }

}
