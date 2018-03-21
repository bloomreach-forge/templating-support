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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.helper.MethodHelper;

/**
 * Utility to register helper functions.
 */
public class HandlebarsHelperRegistrationUtils {

    private static Logger log = LoggerFactory.getLogger(HandlebarsHelperRegistrationUtils.class);

    private HandlebarsHelperRegistrationUtils() {
    }

    /**
     * Register all the declared methods of {@code helperClazz} as helpers with prefixing the helper names
     * by {@code prefix} if a non-null string is provided.
     * @param handlebars handlebars
     * @param prefix prefix
     * @param helperSource helperSource
     * @param helperClazz helperClazz
     * @return handlebars
     */
    @SuppressWarnings("unchecked")
    public static Handlebars registerHelpers(final Handlebars handlebars, final String prefix,
            final Object helperSource, final Class<?> helperClazz) {
        if (helperClazz == Object.class) {
            throw new IllegalArgumentException("helperClazz cannot be java.lang.Object.");
        }

        if (Enum.class.isAssignableFrom(helperClazz)) {
            Enum[] helpers = ((Class<Enum>) helperClazz).getEnumConstants();

            for (Enum helper : helpers) {
                if (helper instanceof Helper) {
                    String helperName = StringUtils.defaultIfBlank(prefix, "") + helper.name();
                    handlebars.registerHelper(helperName, (Helper) helper);
                } else {
                    log.debug("Skipping an enum, {}, as it's not instance of Helper.", helper);
                }
            }

            return handlebars;
        }

        Set<String> overloaded = new HashSet<String>();
        Method[] methods = helperClazz.getDeclaredMethods();

        for (Method method : methods) {
            boolean isPublic = Modifier.isPublic(method.getModifiers());

            String methodName = method.getName();

            if (!isPublic) {
                log.debug("Skipping a method, {}, as it's not public.", method);
                continue;
            }

            if (!CharSequence.class.isAssignableFrom(method.getReturnType())) {
                log.debug("Skipping a method, {}, as its return type is not {}.", method, CharSequence.class.getName());
                continue;
            }

            boolean isStatic = Modifier.isStatic(method.getModifiers());

            if (helperSource == null && !isStatic) {
                log.warn("Skipping a method, {}, as it's not static method and helperSource is null.", method);
                continue;
            }

            final boolean newlyAdded = overloaded.add(methodName);

            if (!newlyAdded) {
                log.warn("Skipping an overloaded method, {}. Only one method with the name, '{}', can be added.",
                        method, methodName);
                continue;
            }

            String helperName = StringUtils.defaultIfBlank(prefix, "") + methodName;
            handlebars.registerHelper(helperName, new MethodHelper(method, (isStatic) ? null : helperSource));
        }

        return handlebars;
    }
}
