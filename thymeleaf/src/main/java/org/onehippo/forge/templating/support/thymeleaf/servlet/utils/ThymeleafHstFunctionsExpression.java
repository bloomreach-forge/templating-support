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

import org.hippoecm.hst.util.ContentBeanUtils;
import org.hippoecm.hst.util.NodeUtils;
import org.hippoecm.hst.utils.PropertyUtils;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

public final class ThymeleafHstFunctionsExpression {


    public boolean isReadable(final Object bean, final String name) {
        return PropertyUtils.isReadable(bean, name);
    }

    public boolean isNodeType(final Node node, final String name) throws RepositoryException {
        return NodeUtils.isNodeType(node, name);
    }

    public boolean isBeanType(final Object bean, final String name) {
        return ContentBeanUtils.isBeanType(bean, name);
    }


}
