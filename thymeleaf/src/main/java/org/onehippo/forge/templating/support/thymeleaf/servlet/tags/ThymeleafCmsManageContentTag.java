/*
 * Copyright 2018-2019 Bloomreach B.V. (http://www.bloomreach.com)
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

import com.google.common.base.Strings;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.onehippo.forge.templating.support.core.helper.CmsEditLinkHelper;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IModel;
import org.thymeleaf.processor.element.IElementModelStructureHandler;

public class ThymeleafCmsManageContentTag extends BaseModelProcessor {
    private static final String TAG_NAME = "manageContent";

    public ThymeleafCmsManageContentTag(final String dialectPrefix) {
        super(dialectPrefix, TAG_NAME);
    }

    @Override
    protected void doProcess(final ITemplateContext context, final IModel model, final IElementModelStructureHandler structureHandler) {
        final HippoBean bean = getAttributeExpression(context, model, "hst:hippobean");
        if (bean == null) {
            return;
        }
        final String comment = CmsEditLinkHelper.INSTANCE.manageContent(bean,
                getAttribute(model, "hst:rootPath"),
                getAttribute(model, "hst:defaultPath"),
                getAttribute(model, "hst:parameterName"),
                getAttribute(model, "hst:templateQuery")
        );
        // remove tag
        model.reset();
        if (!Strings.isNullOrEmpty(comment)) {
            model.insert(0, new TextEvent(comment));
        }
    }
    
}
