/*
 * Copyright 2018-2024 Bloomreach B.V. (http://www.bloomreach.com)
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
package org.onehippo.forge.templating.support.core.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class QueryStringUtils {

    private QueryStringUtils() {
    }

    public static Map<String, String []> parse(String queryString, String encoding) throws UnsupportedEncodingException {
        if (queryString == null) {
            return Collections.emptyMap();
        }

        // keep insertion ordered map to maintain the order of the querystring when re-constructing it from a map
        Map<String, String []> queryParamMap = new LinkedHashMap<>();

        String[] paramPairs = queryString.split("&");
        String paramName;
        String paramValue;
        String [] paramValues;
        String [] tempValues;

        for (String paramPair : paramPairs) {
            String[] paramNameAndValue = paramPair.split("=");

            if (paramNameAndValue.length > 1) {
                paramName = URLDecoder.decode(paramNameAndValue[0], encoding);
                paramValue = URLDecoder.decode(paramNameAndValue[1], encoding);

                paramValues = queryParamMap.get(paramName);

                if (paramValues == null) {
                    queryParamMap.put(paramName, new String[] { paramValue });
                } else {
                    tempValues = new String[paramValues.length + 1];
                    System.arraycopy(paramValues, 0, tempValues, 0, paramValues.length);
                    tempValues[paramValues.length] = paramValue;
                    queryParamMap.put(paramName, tempValues);
                }
            }
        }

        return queryParamMap;
    }

}
