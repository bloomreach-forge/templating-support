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
package org.onehippo.forge.templating.support.handlebars.helper;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.onehippo.forge.templating.support.handlebars.servlet.HandlebarsContextModelMap;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

/**
 * Lenient Method Helper allowing missing parameters, filling in a resonable empty value instead.
 */
public class LenientMethodHelper implements Helper<Object> {

    private static final Object[] NO_ARGS = new Object[0];

    private final Object source;

    private final Method method;

    private final boolean lenient;

    public LenientMethodHelper(final Method method, final Object source) {
        this(method, source, true);
    }

    public LenientMethodHelper(final Method method, final Object source, final boolean lenient) {
        if (method == null) {
            throw new IllegalArgumentException("method must not be null.");
        }

        this.method = method;
        this.source = source;
        this.lenient = lenient;
    }

    @Override
    public Object apply(final Object context, final Options options) throws IOException {
        try {
            Class<?>[] paramTypes = method.getParameterTypes();
            Object[] args = NO_ARGS;

            if (paramTypes.length > 0) {
                args = new Object[paramTypes.length];

                if (args.length == 1 && options.params.length == 0
                        && !HandlebarsContextModelMap.class.isAssignableFrom(paramTypes[0])
                        && (context instanceof HandlebarsContextModelMap)) {
                    args[0] = replaceMissingParameter(paramTypes[0]);
                } else {
                    args[0] = paramTypes[0] == Options.class ? options : context;

                    for (int i = 1; i < args.length; i++) {
                        if (paramTypes[i] == Options.class) {
                            args[i] = options;
                        } else {
                            if (lenient) {
                                if (options.params.length < i) {
                                    args[i] = replaceMissingParameter(paramTypes[i]);
                                } else {
                                    args[i] = options.param(i - 1);
                                }
                            } else {
                                args[i] = options.param(i - 1);
                            }
                        }
                    }
                }
            }

            return (CharSequence) method.invoke(source, args);
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new IllegalArgumentException("could not execute helper: " + method + ", with the given arguments: "
                    + describeParameterTypes(options.params), ex);
        } catch (InvocationTargetException ex) {
            final Throwable cause = ex.getCause();
            if (cause instanceof RuntimeException) {
                return (RuntimeException) cause;
            }
            if (cause instanceof IOException) {
                throw (IOException) cause;
            }
            return new IllegalStateException("could not execute helper: " + method.getName(), cause);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException("could not execute helper: " + method, ex);
        }
    }

    private Object replaceMissingParameter(Class<?> parameterType) {
        if (parameterType == boolean.class || parameterType == Boolean.class) {
            return false;
        } else if (parameterType == short.class || parameterType == Short.class) {
            return (short) 0;
        } else if (parameterType == int.class || parameterType == Integer.class) {
            return 0;
        } else if (parameterType == long.class || parameterType == Long.class) {
            return 0L;
        } else if (parameterType == float.class || parameterType == Float.class) {
            return 0f;
        } else if (parameterType == double.class || parameterType == Double.class) {
            return 0d;
        }

        return null;
    }

    private List<String> describeParameterTypes(final Object[] params) {
        List<String> paramTypes = new ArrayList<>();

        if (params != null) {
            for (Object param : params) {
                if (param == null) {
                    paramTypes.add("null");
                } else {
                    paramTypes.add(param.getClass().getSimpleName());
                }
            }
        }

        return paramTypes;
    }
}
