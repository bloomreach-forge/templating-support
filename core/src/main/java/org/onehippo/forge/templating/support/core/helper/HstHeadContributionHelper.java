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
package org.onehippo.forge.templating.support.core.helper;

import org.apache.commons.lang.StringUtils;
import org.hippoecm.hst.core.component.HeadElement;
import org.hippoecm.hst.core.component.HeadElementImpl;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.container.ContainerConstants;
import org.hippoecm.hst.util.HeadElementUtils;
import org.onehippo.forge.templating.support.core.servlet.TemplateRequestContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.servlet.ServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * HST HeadContribution(s) Helper.
 */
public class HstHeadContributionHelper {

    public static final HstHeadContributionHelper INSTANCE = new HstHeadContributionHelper();

    private HstHeadContributionHelper() {
    }

    public String contributeHeadElement(String headElement, String keyHint, String category) {
        final HstResponse hstResponse = TemplateRequestContext.getHstResponse();
        final String xmlText = StringUtils.trim(headElement);

        if (StringUtils.isNotBlank(xmlText)) {
            if (keyHint == null) {
                keyHint = xmlText;
            }

            if (!hstResponse.containsHeadElement(keyHint)) {
                try {
                    final DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
                    final DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
                    final StringReader reader = new StringReader(xmlText);
                    final Document doc = docBuilder.parse(new InputSource(reader));
                    final Element element = doc.getDocumentElement();

                    if (StringUtils.isNotBlank(category)) {
                        element.setAttribute(ContainerConstants.HEAD_ELEMENT_CONTRIBUTION_CATEGORY_HINT_ATTRIBUTE,
                                category);
                    }

                    hstResponse.addHeadElement(element, keyHint);
                } catch (Exception e) {
                    throw new RuntimeException(e.toString(), e.getCause());
                }
            }
        }

        return "";
    }

    public String contributedHeadElements(String categoryIncludes, String categoryExcludes, final boolean xhtml) {
        final Set<String> categoryIncludesSet = categoryIncludes ==null? null:new LinkedHashSet<>(
                Arrays.asList(StringUtils.split(categoryIncludes, ", \t")));
        final Set<String> categoryExcludesSet = categoryExcludes ==null ? null : new LinkedHashSet<>(
                Arrays.asList(StringUtils.split(categoryExcludes, ", \t")));

        final HstResponse hstResponse = TemplateRequestContext.getHstResponse();
        final List<Element> headElements = hstResponse.getHeadElements();

        StringBuilder buffer = new StringBuilder(256);

        if (headElements != null) {
            for (Element headElement : headElements) {
                if (shouldBeIncludedInOutput(categoryIncludesSet, categoryExcludesSet, headElement)) {
                    buffer.append(headElementToString(headElement, xhtml, hstResponse)).append("\r\n");
                    hstResponse.addProcessedHeadElement(headElement);
                }
            }
        }

        return buffer.toString();
    }

    private boolean shouldBeIncludedInOutput(final Set<String> categoryIncludesSet,
                                             final Set<String> categoryExcludesSet, final Element headElement) {
        boolean filterOnIncludes = categoryIncludesSet != null && !categoryIncludesSet.isEmpty();
        boolean filterOnExcludes = categoryExcludesSet != null && !categoryExcludesSet.isEmpty();

        if (!filterOnIncludes && !filterOnExcludes) {
            return true;
        }

        String category = headElement
                .getAttribute(ContainerConstants.HEAD_ELEMENT_CONTRIBUTION_CATEGORY_HINT_ATTRIBUTE);
        boolean shouldInclude = !filterOnIncludes || categoryIncludesSet.contains(category);
        boolean shouldExclude = filterOnExcludes && categoryExcludesSet.contains(category);

        return shouldInclude && !shouldExclude;
    }

    private String headElementToString(final Element headElement, final boolean xhtml, final ServletResponse response) {
        final Element clone = (Element) headElement.cloneNode(true);
        HeadElement outHeadElement = new HeadElementImpl(clone);

        if (outHeadElement.hasAttribute(ContainerConstants.HEAD_ELEMENT_CONTRIBUTION_CATEGORY_HINT_ATTRIBUTE)) {
            outHeadElement.removeAttribute(ContainerConstants.HEAD_ELEMENT_CONTRIBUTION_CATEGORY_HINT_ATTRIBUTE);
        }
        if (xhtml) {
            return HeadElementUtils.toXhtmlString(outHeadElement, isResponseTextHtmlContent(response));
        } else {
            return HeadElementUtils.toHtmlString(outHeadElement);
        }
    }

    private boolean isResponseTextHtmlContent(final ServletResponse response) {
        String responseContentType = response.getContentType();
        return (responseContentType != null && responseContentType.startsWith("text/html"));
    }
}
