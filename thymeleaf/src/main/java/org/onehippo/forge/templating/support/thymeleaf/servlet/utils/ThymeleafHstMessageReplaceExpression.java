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

package org.onehippo.forge.templating.support.thymeleaf.servlet.utils;

import com.google.common.base.Strings;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.forge.templating.support.core.helper.HstHtmlHelper;
import org.onehippo.forge.templating.support.core.helper.HstMessagesHelper;

import java.io.IOException;
import java.util.ResourceBundle;

public final class ThymeleafHstMessageReplaceExpression {

    
    public String replaceByBundle(final String text, final ResourceBundle bundle) throws IOException {
        return HstMessagesHelper.INSTANCE.replaceByBundle(text, bundle);
    }

    public String replaceByBundle(final HippoHtml html, final boolean fullyQualified, final ResourceBundle bundle) throws IOException {
        if (html == null) {
            return "";
        }
        final String text = HstHtmlHelper.INSTANCE.htmlByHippoHtml(html, fullyQualified);
        if (Strings.isNullOrEmpty(text)) {
            return "";
        }
        return HstMessagesHelper.INSTANCE.replaceByBundle(text, bundle);
    }

    public String replaceByBundleName(final String text, final String bundleName) throws IOException {
        return HstMessagesHelper.INSTANCE.replaceByBundleName(text, bundleName);
    }

    public String replaceByBundleName(final HippoHtml html, final boolean fullyQualified, final String bundleName) throws IOException {
        if (html == null) {
            return "";
        }
        final String text = HstHtmlHelper.INSTANCE.htmlByHippoHtml(html, fullyQualified);
        if (Strings.isNullOrEmpty(text)) {
            return "";
        }
        return HstMessagesHelper.INSTANCE.replaceByBundleName(text, bundleName);
    }

}
