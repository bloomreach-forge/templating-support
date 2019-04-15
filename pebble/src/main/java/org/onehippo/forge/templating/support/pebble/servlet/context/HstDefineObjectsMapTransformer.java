/*
 * Copyright 2019 Hippo B.V. (http://www.onehippo.com)
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
package org.onehippo.forge.templating.support.pebble.servlet.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.Transformer;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.util.HstRequestUtils;

public class HstDefineObjectsMapTransformer implements Transformer {

    private static final String HST_REQUEST = "hstRequest";
    private static final String HST_RESPONSE = "hstResponse";
    private static final String HST_REQUEST_CONTEXT = "hstRequestContext";

    private final HttpServletRequest request;
    private final HttpServletResponse response;

    public HstDefineObjectsMapTransformer(final HttpServletRequest request, final HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public Object transform(Object input) {
        if (HST_REQUEST.equals(input)) {
            return HstRequestUtils.getHstRequest(request);
        } else if (HST_RESPONSE.equals(input)) {
            return HstRequestUtils.getHstResponse(request, response);
        } else if (HST_REQUEST_CONTEXT.equals(input)) {
            return RequestContextProvider.get();
        }

        return null;
    }

}
