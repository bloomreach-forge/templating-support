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
