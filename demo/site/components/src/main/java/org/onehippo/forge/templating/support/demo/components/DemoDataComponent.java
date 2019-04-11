package org.onehippo.forge.templating.support.demo.components;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

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
    public DemoDataComponent() {
    }

    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);
        HstRequestContext context = RequestContextProvider.get();
        HippoBean document = this.getHippoBeanForPath("events/2019/04/breakfast", HippoBean.class);
        request.setAttribute("document", document);
        User contextUser = new User("Context user");
        context.setAttribute("contextUser", contextUser);
        User user = new User("demo user");
        request.setAttribute("user", user);
        request.setAttribute("booleanValueTrue", true);
        request.setAttribute("booleanValueFalse", true);
        HippoFacetNavigationBean facetNavigationBean = ContentBeanUtils.getFacetNavigationBean("blogFacets/Categories/cms", "");
        request.setAttribute("facetBean", facetNavigationBean);
    }
}
