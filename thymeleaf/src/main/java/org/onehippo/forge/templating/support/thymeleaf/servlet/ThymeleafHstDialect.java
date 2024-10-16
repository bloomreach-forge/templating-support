/*
 * Copyright 2018-2024 Bloomreach B.V. (http://www.bloomreach.com)
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

package org.onehippo.forge.templating.support.thymeleaf.servlet;

import org.onehippo.forge.templating.support.thymeleaf.servlet.attributes.*;
import org.onehippo.forge.templating.support.thymeleaf.servlet.tags.*;
import org.onehippo.forge.templating.support.thymeleaf.servlet.utils.ThymeleafHstExpressionFactory;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;
import org.thymeleaf.processor.IProcessor;

import java.util.HashSet;
import java.util.Set;

public class ThymeleafHstDialect extends AbstractProcessorDialect implements IExpressionObjectDialect {


    private static final IExpressionObjectFactory HST_EXPRESSION_FACTORY = new ThymeleafHstExpressionFactory();
    
    public ThymeleafHstDialect() {
        super("HST dialect", "hst", 1000);
    }

    @Override public Set<IProcessor> getProcessors(final String dialectPrefix) {
        final Set<IProcessor> processors = new HashSet<>();
        processors.add(new ThymeleafHstLinkAttribute(dialectPrefix));
        processors.add(new ThymeleafHstWebfilesAttribute(dialectPrefix));
        processors.add(new ThymeleafHstLinkByPathAttribute(dialectPrefix));
        processors.add(new ThymeleafHstHtmlAttribute(dialectPrefix));
        processors.add(new ThymeleafHstHtmlTag(dialectPrefix));
        processors.add(new ThymeleafHstIncludeTag(dialectPrefix));
        processors.add(new ThymeleafHstFacetNavigationLinkAttribute(dialectPrefix));
        processors.add(new ThymeleafHstResourceUrlAttribute(dialectPrefix));
        processors.add(new ThymeleafHstSetBundleTag(dialectPrefix));
        processors.add(new ThymeleafHstHeadContributionsTag(dialectPrefix));
        processors.add(new ThymeleafHstHeadContributionTag(dialectPrefix));
        processors.add(new ThymeleafHstActionUrlAttribute(dialectPrefix));
        processors.add(new ThymeleafHstRenderUrlAttribute(dialectPrefix));
        processors.add(new ThymeleafHstRefItemAttribute(dialectPrefix));
        processors.add(new ThymeleafHstComponentRenderingURLAttribute(dialectPrefix));
        processors.add(new ThymeleafCmsManageContentTag(dialectPrefix));
        processors.add(new ThymeleafCmsEditMenuLinkTag(dialectPrefix));
        return processors;
    }

    @Override public IExpressionObjectFactory getExpressionObjectFactory() {
        return HST_EXPRESSION_FACTORY;
    }
}
