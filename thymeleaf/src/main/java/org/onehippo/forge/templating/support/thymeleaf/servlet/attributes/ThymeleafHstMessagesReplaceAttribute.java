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

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IModel;
import org.thymeleaf.processor.element.AbstractElementModelProcessor;
import org.thymeleaf.processor.element.IElementModelStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

public class ThymeleafHstMessagesReplaceAttribute extends AbstractElementModelProcessor {
    private static final String TAG_NAME = "messageReplace";


    public ThymeleafHstMessagesReplaceAttribute(final String dialectPrefix) {
        super(TemplateMode.HTML, dialectPrefix, TAG_NAME, true, null, false, 1000);
    }

    @Override
    protected void doProcess(final ITemplateContext context, final IModel model, final IElementModelStructureHandler structureHandler) {

/*
        final IEngineConfiguration configuration = context.getConfiguration();
        final IStandardExpressionParser expressionParser = StandardExpressions.getExpressionParser(configuration);

        IProcessableElementTag firstEvent = (IProcessableElementTag) model.get(0);
        final IModelFactory modelFactory = context.getModelFactory();
        final IProcessableElementTag newFirstEvent = modelFactory.removeAttribute(firstEvent, attributeName);
        if (newFirstEvent != firstEvent) {
            model.replace(0, newFirstEvent);
        }

        final StringWriter modelWriter = new StringWriter();
        final TemplateModel cacheModel;
        try {
            model.write(modelWriter);
            cacheModel = configuration.getTemplateManager()
                    .parseString(context.getTemplateData(), modelWriter.toString(), firstEvent.getLine(),
                            firstEvent.getCol(),
                            getTemplateMode(), false);
            final StringWriter templateWriter = new StringWriter();
            configuration.getTemplateManager().process(cacheModel, context, templateWriter);
            log.info("templateWriter {}", templateWriter);
        } catch (IOException e) {

        }*//*
        final IEngineConfiguration configuration = context.getConfiguration();
        final IStandardExpressionParser expressionParser = StandardExpressions.getExpressionParser(configuration);

        IProcessableElementTag firstEvent = (IProcessableElementTag) model.get(0);
        final IModelFactory modelFactory = context.getModelFactory();
        final IProcessableElementTag newFirstEvent = modelFactory.removeAttribute(firstEvent, attributeName);
        if (newFirstEvent != firstEvent) {
            model.replace(0, newFirstEvent);
        }

        final StringWriter modelWriter = new StringWriter();
        final TemplateModel cacheModel;
        try {
            model.write(modelWriter);
            cacheModel = configuration.getTemplateManager()
                    .parseString(context.getTemplateData(), modelWriter.toString(), firstEvent.getLine(),
                            firstEvent.getCol(),
                            getTemplateMode(), false);
            final StringWriter templateWriter = new StringWriter();
            configuration.getTemplateManager().process(cacheModel, context, templateWriter);
            log.info("templateWriter {}", templateWriter);
        } catch (IOException e) {

        }*/


        

    /*    final String variablePrefix = getExpression(context, tag, "hst:variablePrefix");
        final String variableSuffix = getExpression(context, tag, "hst:variableSuffix");
        final String baseName = getExpression(context, tag, "hst:basename");
        final String localeString = getExpression(context, tag, "hst:localeString");
        Locale locale = getExpression(context, tag, "hst:locale");
        ResourceBundle bundle = getExpression(context, tag, "hst:bundle");
        final Character escapeChar = getExpression(context, tag, "hst:escapeChar");
        if (bundle == null) {
            if (locale == null) {
                if (StringUtils.isNotEmpty(localeString)) {
                    locale = LocaleUtils.toLocale(localeString);
                } else {
                    locale = context.getLocale();
                }
            }

            if (StringUtils.isNotEmpty(baseName)) {
                bundle = ResourceBundleUtils.getBundle(baseName, locale);
            }


        }
        if (bundle == null) {
            return;
        }

        final String textContent = ThymeleafHstAttribute.modelAsString(model).toString();
        final String replacedContent = MessageUtils.replaceMessagesByBundle(bundle, textContent, variablePrefix, variableSuffix, escapeChar);
        
*/

    }



}
