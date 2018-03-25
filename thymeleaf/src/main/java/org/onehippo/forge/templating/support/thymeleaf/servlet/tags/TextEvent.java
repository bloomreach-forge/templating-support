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

import org.thymeleaf.model.IModelVisitor;
import org.thymeleaf.model.IText;

import java.io.IOException;
import java.io.Writer;

public class TextEvent implements IText {

    private final String text;

    public TextEvent(final String text) {
        this.text = text;

    }
    
    @Override public int length() {
        return text.length();
    }

    @Override public char charAt(final int index) {
        return text.charAt(index);
    }

    @Override public CharSequence subSequence(final int start, final int end) {
        return text.substring(start, end);
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
        //TODO
    }

    @Override public void write(final Writer writer) throws IOException {
        writer.write(text);
    }

    @Override public String getText() {
        return text;
    }

    @Override public String toString() {
        return text;
    }
}
