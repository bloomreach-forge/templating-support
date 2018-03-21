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
package org.onehippo.forge.templating.support.handlebars.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.helper.MethodHelper;

public class HandlebarsHelperRegistrationUtils {

    private HandlebarsHelperRegistrationUtils() {
    }

    public static Handlebars registerHelpers(final Handlebars handlebars, final String prefix,
            final Object helperSource, final Class<?> helperClazz) {
        if (helperClazz == Object.class) {
            return handlebars;
        }

        if (Enum.class.isAssignableFrom(helperClazz)) {
            Enum[] helpers = ((Class<Enum>) helperClazz).getEnumConstants();

            for (Enum helper : helpers) {
                if (helper instanceof Helper) {
                    String helperName = StringUtils.defaultIfBlank(prefix, "") + helper.name();
                    handlebars.registerHelper(helper.name(), (Helper) helper);
                }
            }

            return handlebars;
        }

        Set<String> overloaded = new HashSet<String>();
        Method[] methods = helperClazz.getDeclaredMethods();

        for (Method method : methods) {
            boolean isPublic = Modifier.isPublic(method.getModifiers());

            String methodName = method.getName();

            if (isPublic && CharSequence.class.isAssignableFrom(method.getReturnType())) {
                boolean isStatic = Modifier.isStatic(method.getModifiers());

                if (helperSource != null || isStatic) {
                    if (overloaded.add(methodName)) {
                        String helperName = StringUtils.defaultIfBlank(prefix, "") + methodName;
                        handlebars.registerHelper(helperName, new MethodHelper(method, helperSource));
                    }
                }
            }
        }

        return handlebars;
    }
}
