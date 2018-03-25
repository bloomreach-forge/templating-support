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

package org.onehippo.forge.templating.support.thymeleaf.servlet.utils;

import org.onehippo.forge.templating.support.core.helper.HstMessagesHelper;

import java.io.IOException;
import java.util.ResourceBundle;

public final class HstThymeleafMessageReplaceExpression {

    
    public String replaceByBundle(final String text, final ResourceBundle bundle) throws IOException {
        return HstMessagesHelper.INSTANCE.replaceByBundle(text, bundle);
    }

    public String replaceByBundleName(final String text, final String bundleName) throws IOException {
        return HstMessagesHelper.INSTANCE.replaceByBundleName(text, bundleName);
    }

}
