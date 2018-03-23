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

package org.onehippo.forge.templating.support.thymeleaf.servlet.attributes;

import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.HashSet;
import java.util.Set;

public class ThymeleafHstDialect extends AbstractProcessorDialect {


    public ThymeleafHstDialect() {
        super("HST dialect", "hst", 1000);
    }

    @Override public Set<IProcessor> getProcessors(final String dialectPrefix) {
        final Set<IProcessor> processors = new HashSet<>();
        processors.add(new ThymeleafHstLinkAttribute(dialectPrefix));
        processors.add(new ThymeleafHstWebfilesAttribute(dialectPrefix));
        processors.add(new ThymeleafHstPathAttribute(dialectPrefix));
        processors.add(new ThymeleafHstHtmlAttribute(dialectPrefix));
        processors.add(new ThymeleafHstIncludeAttribute(dialectPrefix));
        processors.add(new ThymeleafHstResourceUrlAttribute(dialectPrefix));
        processors.add(new ThymeleafHstActionUrlAttribute(dialectPrefix));
        processors.add(new ThymeleafHstHeadContributionInsertTag(dialectPrefix));
        processors.add(new ThymeleafHstHeadContributionTag(dialectPrefix));
        processors.add(new ThymeleafHstRenderUrlAttribute(dialectPrefix));
        return processors;
    }
}
