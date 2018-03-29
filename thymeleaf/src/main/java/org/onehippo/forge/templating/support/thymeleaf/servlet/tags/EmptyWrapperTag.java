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

package org.onehippo.forge.templating.support.thymeleaf.servlet.tags;

import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.engine.ElementDefinition;
import org.thymeleaf.model.*;
import org.thymeleaf.templatemode.TemplateMode;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class EmptyWrapperTag implements ICloseElementTag, IOpenElementTag, IProcessableElementTag {

    private static final IAttribute[] EMPTY_ATTRIBUTES = new IAttribute[0];


    @Override public boolean isUnmatched() {
        return false;
    }

    @Override public TemplateMode getTemplateMode() {
        return null;
    }

    @Override public String getElementCompleteName() {
        return null;
    }

    @Override public ElementDefinition getElementDefinition() {
        return null;
    }

    @Override public boolean isSynthetic() {
        return true;
    }

    @Override public boolean hasLocation() {
        return false;
    }

    @Override public String getTemplateName() {
        return null;
    }

    @Override public int getLine() {
        return 0;
    }

    @Override public int getCol() {
        return 0;
    }

    @Override public void accept(final IModelVisitor visitor) {

    }

    @Override public void write(final Writer writer) throws IOException {

    }

    @Override public IAttribute[] getAllAttributes() {
        return EMPTY_ATTRIBUTES;
    }

    @Override public Map<String, String> getAttributeMap() {
        return null;
    }

    @Override public boolean hasAttribute(final String completeName) {
        return false;
    }

    @Override public boolean hasAttribute(final String prefix, final String name) {
        return false;
    }

    @Override public boolean hasAttribute(final AttributeName attributeName) {
        return false;
    }

    @Override public IAttribute getAttribute(final String completeName) {
        return null;
    }

    @Override public IAttribute getAttribute(final String prefix, final String name) {
        return null;
    }

    @Override public IAttribute getAttribute(final AttributeName attributeName) {
        return null;
    }

    @Override public String getAttributeValue(final String completeName) {
        return null;
    }

    @Override public String getAttributeValue(final String prefix, final String name) {
        return null;
    }

    @Override public String getAttributeValue(final AttributeName attributeName) {
        return null;
    }
}
