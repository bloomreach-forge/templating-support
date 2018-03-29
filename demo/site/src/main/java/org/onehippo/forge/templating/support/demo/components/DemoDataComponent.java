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

package org.onehippo.forge.templating.support.demo.components;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.util.ContentBeanUtils;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.onehippo.forge.templating.support.demo.beans.User;

public class DemoDataComponent extends CommonComponent {

    @Override public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);
        // add template data:
        final HstRequestContext context = RequestContextProvider.get();
        final HippoBean document = getHippoBeanForPath("events/2018/03/breakfast", HippoBean.class);
        request.setAttribute("document", document);
        final User contextUser = new User("Context user");
        context.setAttribute("contextUser", contextUser);
        final User user = new User("demo user");
        request.setAttribute("user", user);
        request.setAttribute("booleanValueTrue", true);
        request.setAttribute("booleanValueFalse", true);
        final HippoFacetNavigationBean facetNavigationBean = ContentBeanUtils.getFacetNavigationBean("blogFacets/Categories/cms", "");
        request.setAttribute("facetBean", facetNavigationBean);

    }
}
