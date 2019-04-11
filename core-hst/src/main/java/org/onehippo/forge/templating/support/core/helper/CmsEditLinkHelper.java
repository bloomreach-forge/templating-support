/*
 * Copyright 2018-2019 Bloomreach B.V. (http://www.bloomreach.com)
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
package org.onehippo.forge.templating.support.core.helper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.servlet.ServletRequest;

import org.apache.commons.lang.StringUtils;
import org.hippoecm.hst.configuration.ConfigurationUtils;
import org.hippoecm.hst.configuration.internal.CanonicalInfo;
import org.hippoecm.hst.configuration.internal.ConfigurationLockInfo;
import org.hippoecm.hst.configuration.site.HstSite;
import org.hippoecm.hst.configuration.sitemenu.HstSiteMenuConfiguration;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.channelmanager.ChannelManagerConstants;
import org.hippoecm.hst.core.component.HstComponent;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.container.HstComponentWindow;
import org.hippoecm.hst.core.parameters.JcrPath;
import org.hippoecm.hst.core.parameters.Parameter;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.core.request.ComponentConfiguration;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.core.request.ResolvedMount;
import org.hippoecm.hst.core.sitemenu.CommonMenu;
import org.hippoecm.hst.util.ParametersInfoAnnotationUtils;
import org.hippoecm.repository.api.HippoNode;
import org.hippoecm.repository.api.HippoNodeType;
import org.hippoecm.repository.util.JcrUtils;
import org.onehippo.forge.templating.support.core.servlet.TemplateRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hippoecm.hst.core.channelmanager.ChannelManagerConstants.HST_LOCKED_BY;
import static org.hippoecm.hst.core.channelmanager.ChannelManagerConstants.HST_LOCKED_BY_CURRENT_USER;
import static org.hippoecm.hst.core.channelmanager.ChannelManagerConstants.HST_LOCKED_ON;
import static org.hippoecm.hst.core.container.ContainerConstants.CMS_REQUEST_USER_ID_ATTR;
import static org.hippoecm.hst.core.container.ContainerConstants.RENDER_VARIANT;
import static org.hippoecm.hst.utils.TagUtils.encloseInHTMLComment;
import static org.hippoecm.hst.utils.TagUtils.toJSONMap;

/**
 * For CMS Edit Link related tags.
 */
public final class CmsEditLinkHelper {

    private static final Logger log = LoggerFactory.getLogger(CmsEditLinkHelper.class);

    public static final CmsEditLinkHelper INSTANCE = new CmsEditLinkHelper();

    // It seems okay to return null in Handlebars at least for example. Is it okay in Thymeleaf or elsewhere?
    private static final String NO_RESULT = null;

    private CmsEditLinkHelper() {
    }

    public String manageContent(final HippoBean bean, final String rootPath, final String defaultPath, final String parameterName, final String templateQuery) {
        final HstRequestContext requestContext = RequestContextProvider.get();
        if (invalidCmsRequest(bean, requestContext)) {
            return NO_RESULT;
        }

        String documentId = null;
        if (bean != null) {
            final HippoNode documentNode = (HippoNode) bean.getNode();
            try {
                final Node editNode = documentNode.getCanonicalNode();
                if (editNode == null) {
                    log.debug("Cannot create a manageContent tag, cannot find canonical node of '{}'", documentNode.getPath());
                    return NO_RESULT;
                }

                final Node handleNode = getHandleNodeIfIsAncestor(editNode);
                if (handleNode == null) {
                    log.warn("Could not find handle node of {}", editNode.getPath());
                    return NO_RESULT;
                }

                log.debug("The node path for the manageContent tag is '{}'", handleNode.getPath());
                documentId = handleNode.getIdentifier();
            } catch (RepositoryException e) {
                log.warn("Error while retrieving the handle of '{}', skipping manageContent tag",
                        JcrUtils.getNodePathQuietly(bean.getNode()), e);
                return NO_RESULT;
            }
        }

        final HstRequest request = TemplateRequestContext.getHstRequest();
        final JcrPath jcrPath = getJcrPath(parameterName, request);
        final boolean isRelativePathParameter = jcrPath != null && jcrPath.isRelative();
        if (isRelativePathParameter && StringUtils.startsWith(rootPath, "/")) {
            log.warn("Ignoring manageContent tag in template '{}' for component parameter '{}':"
                            + " the @{} annotation of the parameter makes it store a relative path to the"
                            + " content root of the channel while the 'rootPath' attribute of the manageContent"
                            + " tag points to the absolute path '{}'."
                            + " Either make the root path relative to the channel content root,"
                            + " or make the component parameter store an absolute path.",
                    getComponentRenderPath(), parameterName, JcrPath.class.getSimpleName(), rootPath);
            return NO_RESULT;
        }

        String absoluteRootPath;
        try {
            absoluteRootPath = checkRootPath(rootPath, requestContext);
        } catch (final RepositoryException e) {
            log.warn("Exception while checking rootPath parameter for manageContent tag in template '{}'.",
                    getComponentRenderPath(), e);
            return NO_RESULT;
        }
        final String componentValue = getComponentValue(parameterName, isRelativePathParameter);

        return write(templateQuery, rootPath, defaultPath, parameterName, documentId, componentValue, jcrPath, isRelativePathParameter, absoluteRootPath);

    }

    public String cmsEditMenuLink(final CommonMenu menu) {
        final HstRequestContext requestContext = RequestContextProvider.get();
        if (invalidCmsRequest(menu, requestContext)) {
            return NO_RESULT;
        }

        if (menu == null) {
            log.warn("Cannot create a cms edit menu because no menu present");
            return NO_RESULT;
        }

        final HstSite hstSite = requestContext.getResolvedMount().getMount().getHstSite();
        if (hstSite == null) {
            log.debug("Skipping cms edit menu because no hst site for matched mount '{}'.",
                    requestContext.getResolvedMount().getMount().toString());
            return NO_RESULT;
        }

        final HstSiteMenuConfiguration siteMenuConfiguration = hstSite.getSiteMenusConfiguration().getSiteMenuConfiguration(menu.getName());

        if (siteMenuConfiguration == null) {
            log.debug("Skipping cms edit menu because no siteMenuConfiguration '{}' found for matched mount '{}'.",
                    menu.getName(), requestContext.getResolvedMount().getMount().toString());
            return NO_RESULT;
        }

        if (!(siteMenuConfiguration instanceof CanonicalInfo)) {
            log.debug("Skipping cms edit menu because siteMenuConfiguration found not instanceof CanonicalInfo " +
                    "for matched mount '{}'.", requestContext.getResolvedMount().getMount().toString());
            return NO_RESULT;
        }
        if (!(siteMenuConfiguration instanceof ConfigurationLockInfo)) {
            log.debug("Skipping cms edit menu because siteMenuConfiguration found not instanceof ConfigurationLockInfo " +
                    "for matched mount '{}'.", requestContext.getResolvedMount().getMount().toString());
            return NO_RESULT;
        }
        CanonicalInfo canonicalInfo = (CanonicalInfo) siteMenuConfiguration;
        if (!canonicalInfo.isWorkspaceConfiguration()) {
            log.debug("Skipping cms edit menu because siteMenuConfiguration found not part of workspace " +
                    "for matched mount '{}'.", requestContext.getResolvedMount().getMount().toString());
            return NO_RESULT;
        }
        if (!canonicalInfo.getCanonicalPath().startsWith(hstSite.getConfigurationPath() + '/')) {
            log.debug("Skipping cms edit menu because siteMenuConfiguration found is inherited from other configuration " +
                    "for matched mount '{}'.", requestContext.getResolvedMount().getMount().toString());
            return NO_RESULT;
        }
        return encloseInHTMLComment(toJSONMap(getAttributeMap(siteMenuConfiguration)));
    }

    private Map<?, ?> getAttributeMap(final HstSiteMenuConfiguration siteMenuConfiguration) {
        final String canonicalIdentifier = ((CanonicalInfo) siteMenuConfiguration).getCanonicalIdentifier();
        final Map<String, Object> result = new HashMap<>();
        result.put(ChannelManagerConstants.HST_TYPE, "EDIT_MENU_LINK");
        result.put("uuid", canonicalIdentifier);
        final String lockedBy = ((ConfigurationLockInfo) siteMenuConfiguration).getLockedBy();
        if (lockedBy != null) {
            result.put(HST_LOCKED_BY, lockedBy);
            result.put(HST_LOCKED_BY_CURRENT_USER, lockedBy.equals(getCurrentCmsUser()));
            result.put(HST_LOCKED_ON, ((ConfigurationLockInfo) siteMenuConfiguration).getLockedOn().getTimeInMillis());
        }
        return result;
    }

    private static String getCurrentCmsUser() {
        return (String) RequestContextProvider.get().getServletRequest().getAttribute(CMS_REQUEST_USER_ID_ATTR);
    }
    private String write(final String templateQuery, final String rootPath, final String defaultPath, final String parameterName, final String documentId, final String componentValue, final JcrPath jcrPath,
                         final boolean isRelativePathParameter, final String absoluteRootPath) {

        final Map<String, Object> attributeMap = getAttributeMap( templateQuery,   rootPath, defaultPath,   parameterName, documentId, componentValue, jcrPath, isRelativePathParameter, absoluteRootPath);
        return encloseInHTMLComment(toJSONMap(attributeMap));
    }

    private JcrPath getJcrPath(final String parameterName, final ServletRequest request) {
        if (parameterName == null) {
            return null;
        }

        final HstComponentWindow window = (HstComponentWindow) request.getAttribute("org.hippoecm.hst.core.container.HstComponentWindow");
        final HstComponent component = window.getComponent();
        final ComponentConfiguration componentConfig = component.getComponentConfiguration();
        final ParametersInfo paramsInfo = ParametersInfoAnnotationUtils.getParametersInfoAnnotation(component, componentConfig);
        return getParameterAnnotation(paramsInfo, parameterName, JcrPath.class);
        //return ParameterUtils.getParameterAnnotation(paramsInfo, parameterName, JcrPath.class);
    }

    private boolean invalidCmsRequest(final Object bean, final HstRequestContext requestContext) {
        return requestContext == null || !requestContext.isCmsRequest() || bean == null;
    }

    private String getComponentValue(final String parameterName, final boolean isRelativePathParameter) {
        if (parameterName == null) {
            return null;
        }

        final HstComponentWindow window = getCurrentComponentWindow(TemplateRequestContext.getHstRequest());
        final String prefixedParameterName = getPrefixedParameterName(window, parameterName);
        final String componentValue = window.getParameter(prefixedParameterName);

        if (componentValue != null && isRelativePathParameter) {
            return getChannelRootPath() + '/' + componentValue;
        }
        return componentValue;
    }

    private String getChannelRootPath() {
        final ResolvedMount resolvedMount = RequestContextProvider.get().getResolvedMount();
        return resolvedMount.getMount().getContentPath();
    }

    private String checkRootPath(final String rootPath, final HstRequestContext requestContext) throws RepositoryException {
        if (rootPath == null) {
            return null;
        }

        final String absoluteRootPath = getAbsoluteRootPath(rootPath, requestContext);

        try {
            final Node rootPathNode = requestContext.getSession().getNode(absoluteRootPath);
            if (!rootPathNode.isNodeType("hippostd:folder") && !rootPathNode.isNodeType("hippostd:directory")) {
                log.warn("Rootpath '{}' is not a folder node. Parameters rootPath and defaultPath of manageContent tag"
                        + " in template '{}' are ignored.", rootPath, getComponentRenderPath());
                return null;
            }
        } catch (final PathNotFoundException e) {
            log.warn("Rootpath '{}' does not exist. Parameters rootPath and defaultPath of manageContent tag"
                    + " in template '{}' are ignored.", rootPath, getComponentRenderPath());

            return null;
        }
        return absoluteRootPath;
    }

    private String getComponentRenderPath() {
        final HstComponentWindow window = getCurrentComponentWindow(TemplateRequestContext.getHstRequest());
        if (window == null) {
            return "";
        }
        final HstComponent component = window.getComponent();
        if (component == null) {
            return "";
        }
        final ComponentConfiguration componentConfiguration = component.getComponentConfiguration();
        if (componentConfiguration == null) {
            return "";
        }
        return componentConfiguration.getRenderPath();
    }

    private String getAbsoluteRootPath(final String rootPath, final HstRequestContext requestContext) {
        if (StringUtils.startsWith(rootPath, "/")) {
            return rootPath;
        } else {
            return '/' + requestContext.getSiteContentBasePath() + '/' + rootPath;
        }
    }


    private HstComponentWindow getCurrentComponentWindow(final ServletRequest request) {
        return (HstComponentWindow) request.getAttribute(org.hippoecm.hst.core.container.ContainerConstants.HST_COMPONENT_WINDOW);
    }

    private String getPrefixedParameterName(final HstComponentWindow window, final String parameterName) {
        final Object parameterPrefix = window.getAttribute(RENDER_VARIANT);

        if (parameterPrefix == null || parameterPrefix.equals("")) {
            return parameterName;
        }

        return ConfigurationUtils.createPrefixedParameterName(parameterPrefix.toString(), parameterName);
    }

    private Map<String, Object> getAttributeMap(final String templateQuery, final String rootPath, final String defaultPath,final String parameterName,
                                                final String documentId, final String componentValue, final JcrPath jcrPath,
                                                final boolean isRelativePathParameter, final String absoluteRootPath) {
        final Map<String, Object> result = new LinkedHashMap<>();
        writeToMap(result, ChannelManagerConstants.HST_TYPE, "MANAGE_CONTENT_LINK");
        writeToMap(result, "uuid", documentId);
        writeToMap(result, "templateQuery", templateQuery);
        writeToMap(result, "rootPath", rootPath);
        writeToMap(result, "defaultPath", defaultPath);
        writeToMap(result, "parameterName", parameterName);

        if (parameterName != null) {
            writeToMap(result, "parameterValueIsRelativePath", Boolean.toString(isRelativePathParameter));
            writeToMap(result, "parameterValue", componentValue);
        }

        if (jcrPath != null) {
            final String pickerRootPath = getFirstNonBlankString(absoluteRootPath, jcrPath.pickerRootPath(), getChannelRootPath());
            writeToMap(result, "pickerConfiguration", jcrPath.pickerConfiguration());
            writeToMap(result, "pickerInitialPath", getPickerInitialPath(jcrPath.pickerInitialPath(), pickerRootPath));
            writeToMap(result, "pickerRemembersLastVisited", Boolean.toString(jcrPath.pickerRemembersLastVisited()));
            writeToMap(result, "pickerRootPath", pickerRootPath);

            final String nodeTypes = Arrays.stream(jcrPath.pickerSelectableNodeTypes()).collect(Collectors.joining(","));
            writeToMap(result, "pickerSelectableNodeTypes", nodeTypes);
        }

        return result;
    }

    private static void writeToMap(final Map<String, Object> result, final String key, final String value) {
        if (StringUtils.isNotEmpty(value)) {
            result.put(key, value);
        }
    }

    /**
     * Get the first String that is not blank from a number of Strings.
     *
     * @param strings variable list of Strings
     * @return first non-null and not empty String or null if all are blank
     */
    private String getFirstNonBlankString(final String... strings) {
        return Arrays.stream(strings).filter(StringUtils::isNotBlank).findFirst().orElse(null);
    }

    private String getPickerInitialPath(final String pickerInitialPath, final String prependRootPath) {
        if ("".equals(pickerInitialPath) || pickerInitialPath.startsWith("/")) {
            return pickerInitialPath;
        } else {
            return prependRootPath + (prependRootPath.endsWith("/") ? "" : "/") + pickerInitialPath;
        }
    }

    /*
     * when a currentNode is of type hippo:handle, we return this node, else we check the parent, until we are at the jcr root node.
     * When we hit the jcr root node, we return null;
     */
    private static Node getHandleNodeIfIsAncestor(final Node currentNode) throws RepositoryException {
        final Node rootNode = currentNode.getSession().getRootNode();
        return getHandleNodeIfIsAncestor(currentNode, rootNode);
    }

    private static Node getHandleNodeIfIsAncestor(Node currentNode, Node rootNode) throws RepositoryException {
        if (currentNode.isNodeType(HippoNodeType.NT_HANDLE)) {
            return currentNode;
        }
        if (currentNode.isSame(rootNode)) {
            return null;
        }
        return getHandleNodeIfIsAncestor(currentNode.getParent(), rootNode);
    }

    /**
     * Returns an annotation on a 'parameter method' in a parameters info class, i.e. a method that is annotated
     * with {@link @Parameter}.
     *
     * @param parametersInfo  the parameters info class to analyze
     * @param parameterName   the name of the parameter as returned by {@link Parameter#name()}
     * @param annotationClass the class of the annotation to find
     * @param <A>             the annotation, or null if the annotation could not be found
     * @return
     */
    private static <A extends Annotation> A getParameterAnnotation(final ParametersInfo parametersInfo,
                                                                  final String parameterName,
                                                                  final Class<A> annotationClass) {
        if (parametersInfo == null || parameterName == null || annotationClass == null) {
            return null;
        }

        final Class<?> paramsInfoClass = parametersInfo.type();
        for (Method method : paramsInfoClass.getMethods()) {
            final Parameter parameter = method.getAnnotation(Parameter.class);
            if (parameter != null && parameter.name().equals(parameterName)) {
                return method.getAnnotation(annotationClass);
            }
        }

        return null;
    }
}
