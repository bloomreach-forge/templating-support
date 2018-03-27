
[//]: # (  Copyright 2018 Hippo B.V. (http://www.onehippo.com)
[//]: # (  )
[//]: # (  Licensed under the Apache License, Version 2.0 (the "License")
[//]: # (  you may not use this file except in compliance with the License.  )
[//]: # (  You may obtain a copy of the License at  )
[//]: # (  )
[//]: # (       http://www.apache.org/licenses/LICENSE-2.0  )
[//]: # (  )
[//]: # (  Unless required by applicable law or agreed to in writing, software  )
[//]: # (  distributed under the License is distributed on an "AS IS" BASIS,  )
[//]: # (  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  )
[//]: # (  See the License for the specific language governing permissions and  )
[//]: # (  limitations under the License.  )

## How Thymeleaf Templating Support Module support HST-2 Integration?

Thymeleaf Templating Support Module provides equivalent features to HST-2 JSP Tab Libraries.

### Generating HST Link like *<@hst.link />* tag

```html

<a hst:link="${document}" th:text="${document.title}"></a>

```

| Attribute name                        | Value         							|
| ---------------------                 |:----------------------------------------:	|
| hst:link (**required**)      		    | HippoBean, HstLink, String (linkByPath) 	|
| hst:fullyQualified (**optional**)     | boolean, string(true/false)    |



### Generating HST WebFile Link like *<@hst.webfile />* tag


```html

<link hst:webfile="/thymeleaf/webfiles.css" />

or:

<link rel="stylesheet" th:href="@{webfile:/css/bootstrap.css}" type="text/css"/>

```

| Attribute name                        | Value         							|
| ---------------------                 |:----------------------------------------:	|
| hst:webfile (**required**)      		| String,	|
| hst:fullyQualified (**optional**)     | boolean, string(true/false)    |



### Rendering Hippo HTML Compound Bean like *<@hst.html />* tag

```html

<div hst:html="${document.html}" />

```

| Attribute name                        | Value         							|
| ---------------------                 |:----------------------------------------:	|
| hst:html (**required**)      		| HippoHtml,	|
| hst:fullyQualified (**optional**)     | boolean, string(true/false)    |
| hst:imageVariantName (**optional**)     | string    |
| hst:imageVariantReplaces (**optional**)     | string    |
| hst:imageVariantFallback (**optional**)     | boolean, string(true/false)    |
| hst:canonicalLinks (**optional**)     | boolean, string(true/false)    |
| hst:contentRewriter (**optional**)     | ContentRewriter<String>  |

or by using tag:

```html

<hst:html hst:htmlBean="${document.html}" />

```

| Attribute name                        | Value         							|
| ---------------------                 |:----------------------------------------:	|
| hst:htmlBean (**required**)      		| HippoHtml,	|
| hst:fullyQualified (**optional**)     | boolean, string(true/false)    |
| hst:imageVariantName (**optional**)     | string    |
| hst:imageVariantReplaces (**optional**)     | string    |
| hst:imageVariantFallback (**optional**)     | boolean, string(true/false)    |
| hst:canonicalLinks (**optional**)     | boolean, string(true/false)    |
| hst:contentRewriter (**optional**)     | ContentRewriter<String>  |






### Including Child HST Components like *<@hst.include />* tag

```html

 <hst:include hst:ref="footer-thymeleaf"/>

```

| Attribute name                        | Value         							|
| ---------------------                 |:----------------------------------------:	|
| hst:ref (**required**)     | string(true/false)    |



### Generating HST Render URLs like *<@hst.renderURL />* tag


```html

<a hst:renderURL="a=1&b=2">renderURL</a>

```

| Attribute name                        | Value         							|
| ---------------------                 |:----------------------------------------:	|
| hst:renderURL (**required**)      		    | optional value (parameters e.g. a=1&b=2)	|




### Generating HST Action URLs like *<@hst.actionURL />* tag



```html

<a hst:actionURL="">actionURL</a>

```

| Attribute name                        | Value         							|
| ---------------------                 |:----------------------------------------:	|
| hst:actionURL (**required**)      		    | optional value (parameters e.g. a=1&b=2)	|




### Generating HST Resource URLs like *<@hst.resourceURL />* tag


```html

<a hst:resourceURL="">resourceURL</a>

```

| Attribute name                        | Value         							|
| ---------------------                 |:----------------------------------------:	|
| hst:resourceURL (**required**)      		    | optional value (parameters e.g. a=1&b=2)	|



### Generating HST ComponentRendering URLs like *<@hst.componentRenderingURL />* tag
```html

<a hst:componentRenderingURL="">componentRenderingURL</a>

```

| Attribute name                        | Value         							|
| ---------------------                 |:----------------------------------------:	|
| hst:componentRenderingURL (**required**)      		    | optional value (parameters e.g. a=1&b=2)	|



### Contributing HST Head Element like *<@hst.headContribution />* tag

```html


  <hst:headContribution hst:category="thymeleafCategory">
    <link hst:webfile="/thymeleaf/webfiles.css" />
  </hst:headContribution>

```

| Attribute name                        | Value         							|
| ---------------------                 |:----------------------------------------:	|
| hst:category (**optional**)      		    |  String  	|
| hst:keyHint (**optional**)      		    |  String  	|




### Writing All the Contributed HST Head Elements like *<@hst.headContributions />* tag


```html


<hst:headContributions hst:categoryIncludes="htmlBodyEnd, scripts" hst:xhtml="true"/>
<hst:headContributions hst:categoryExcludes="htmlBodyEnd, scripts" hst:xhtml="true" />

```

| Attribute name                        | Value         							|
| ---------------------                 |:----------------------------------------:	|
| hst:xhtml (**optional**)      		    |  boolean, string(true/false)   	|
| hst:categoryIncludes (**optional**)      		    |  String  	|
| hst:categoryExcludes (**optional**)      		    |  String  	|



### Message Replacing like *<@hst.messagesReplace />* tag
This is implemeted as Thymeleaf expression:

```java

#messagesReplace.replaceByBundle(final String text, final ResourceBundle bundle)
#messagesReplace.replaceByBundleName(final String text, final String bundleName)


```

### CMS edit link, like  *<@hst.cmsEditLink />* tag


```html

<a hst:cmsEditLink="${document}">Link</a>

```

| Attribute name                        | Value         							|
| ---------------------                 |:----------------------------------------:	|
| hst:cmsEditLink (**required**)      		    |  HippoBean   	|


### CMS edit menu, like  *<@hst.cmseditmenu />* tag


```html

<hst:cmseditmenu hst:menu="${menu}" />

```

| Attribute name                        | Value         							|
| ---------------------                 |:----------------------------------------:	|
| hst:menu (**required**)      		    |  CommonMenu   	|


### CMS menage content, like  *<@hst.manageContent />* tag


```html

<hst:manageContent hst:hippobean="${document}" />

```

| Attribute name                        | Value         							|
| ---------------------                 |:----------------------------------------:	|
| hst:hippobean (**required**)      		    |  CommonMenu   	|


