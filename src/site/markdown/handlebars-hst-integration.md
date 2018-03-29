
[//]: # (  Copyright 2018 Hippo B.V. (http://www.onehippo.com)  )
[//]: # (  )
[//]: # (  Licensed under the Apache License, Version 2.0 (the "License");  )
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

## How Handlebars Templating Support Module support HST-2 Integration?

Handlebars Templating Support Module provides equivalent features to HST-2 JSP Tab Libraries.

## Models

HstRequestContext, HstRequest and HstResponse objects can always be accessed through ```hstRequestContext```, ```hstRequest``` and ```hstResponse```.

## Helpers

### hst:linkByHippoBean

Synopsis: generate an HST link by the given ```HippoBean``` with the following parameter(s):

| Index | Type      | Description                                           | Required? | Default value |
|:-----:|:---------:|-------------------------------------------------------|-----------|---------------|
| 0     | HippoBean | Content bean to link to.                              | Yes       |               |
| 1     | boolean   | whether to generate link as a fully qualified URL     | No        | false         |

Example(s):

```
{{hst:linkByHippoBean document}}
```

### hst:linkByPath

Synopsis: generate an HST link by the given path info with the following parameter(s):

| Index | Type      | Description                                           | Required? | Default value |
|:-----:|:---------:|-------------------------------------------------------|-----------|---------------|
| 0     | String    | Content path to link to.                              | Yes       |               |
| 1     | boolean   | whether to generate link as fully qualified URL       | No        | false         |

Example(s):

```
{{hst:linkByPath "/news"}}
```

### hst:linkBySiteMapItemRefId

Synopsis: generate an HST link by the given sitemap item reference ID with the following parameter(s):

| Index | Type      | Description                                           | Required? | Default value |
|:-----:|:---------:|-------------------------------------------------------|-----------|---------------|
| 0     | String    | Sitemap item reference ID to link to.                 | Yes       |               |
| 1     | boolean   | whether to generate link as fully qualified URL       | No        | false         |

Example(s):

```
{{hst:linkBySiteMapItemRefId "root"}}
```

### hst:linkForFacet

Synopsis: generate an HST link by the given HippoFacetSubNavigation items with the following parameter(s):

| Index | Type                                | Description                                           | Required? | Default value |
|:-----:|:-----------------------------------:|-------------------------------------------------------|-----------|---------------|
| 0     | HippoFacetSubNavigation             | Current HippoFacetSubNavigation object.               | Yes       |               |
| 1     | HippoFacetSubNavigation             | HippoFacetSubNavigation object to remove              | No        |               |
| 2     | List&lt;HippoFacetSubNavigation&gt; | The list of HippoFacetSubNavigations to remove.       | Yes       |               |

Example(s):

```
{{hst:linkForFacet curSubNav removeItem removeList}}
```

### hst:webfileByPath

Synopsis: generate an HST WebFiles link by the given path with the following parameter(s):

| Index | Type      | Description                                           | Required? | Default value |
|:-----:|:---------:|-------------------------------------------------------|-----------|---------------|
| 0     | String    | WebFile path to link to.                              | Yes       |               |
| 1     | boolean   | whether to generate link as a fully qualified URL     | No        | false         |

Example(s):

```
{{hst:webfileByPath "/js/bootstrap.min.js"}}
```

### hst:htmlByHippoHtml

Synopsis: rewrite the given HippoHtmlBean content to HTML markups with the following parameter(s):

| Index | Type          | Description                                           | Required? | Default value |
|:-----:|:-------------:|-------------------------------------------------------|-----------|---------------|
| 0     | HippoHtmlBean | HippoHtmlBean instance.                               | Yes       |               |
| 1     | boolean       | whether to generate link as a fully qualified URL     | No        | false         |

Example(s):

```
{{hst:htmlByHippoHtml document.body}}
```

### hst:htmlByFormattedText

Synopsis: rewrite the given formatted text string to HTML markups with the following parameter(s):

| Index | Type          | Description                                           | Required? | Default value |
|:-----:|:-------------:|-------------------------------------------------------|-----------|---------------|
| 0     | String        | Formatted text                                        | Yes       |               |
| 1     | boolean       | whether to generate link as a fully qualified URL     | No        | false         |

Example(s):

```
{{hst:htmlByFormattedText document.introduction}}
```

### hst:rewriteHippoHtml

Synopsis: rewrite the given HippoHtmlBean content to HTML markups with the following parameter(s):

| Index | Type                          | Description                                             | Required? | Default value |
|:-----:|:-----------------------------:|---------------------------------------------------------|-----------|---------------|
| 0     | ContentRewriter&lt;String&gt; | HippoHtmlBean instance.                                 | Yes       |               |
| 1     | HippoHtmlBean                 | HippoHtmlBean instance.                                 | Yes       |               |
| 2     | ImageVariant                  | HippoHtmlBean instance.                                 | No        |               |
| 3     | boolean                       | whether the ContentRewriter should use an imageVariant. | No        | false         |
| 4     | boolean                       | whether to generate link as a fully qualified URL       | No        | false         |

Example(s):

```
{{hst:rewriteHippoHtml contentRewriter document.body}}
```

### hst:includeChild

Synopsis: Include the child component window's output after finding it by the given child component window reference name with the following parameter(s):

| Index | Type          | Description                                           | Required? | Default value |
|:-----:|:-------------:|-------------------------------------------------------|-----------|---------------|
| 0     | String        | Child component window reference name                | Yes       |               |

Example(s):

```    
    {{{hst:includeChild "container"}}}
 
```

### hst:renderURL

Synopsis: Generate an HST Render URL:

| Index | Type          | Description                                               | Required? | Default value |
|:-----:|:-------------:|-----------------------------------------------------------|-----------|---------------|
| 0     | String        | Additional Request Parameters in HTTP Query String format | No        |               |

Example(s):

```
{{hst:renderURL}}
{{hst:renderURL "a=1&b=2"}}
```

### hst:actionURL

Synopsis: Generate an HST Action URL:

| Index | Type          | Description                                               | Required? | Default value |
|:-----:|:-------------:|-----------------------------------------------------------|-----------|---------------|
| 0     | String        | Additional Request Parameters in HTTP Query String format | No        |               |

Example(s):

```
{{hst:actionURL}}
{{hst:actionURL "a=1&b=2"}}
```

### hst:resourceURL

Synopsis: Generate an HST Resource URL:

| Index | Type          | Description                                               | Required? | Default value |
|:-----:|:-------------:|-----------------------------------------------------------|-----------|---------------|
| 0     | String        | Resource ID                                               | Yes       |               |
| 1     | String        | Additional Request Parameters in HTTP Query String format | No        |               |

Example(s):

```
{{hst:resourceURL "aResourceID"}}
```

### hst:componentRenderingURL

Synopsis: Generate an HST Component Rendering URL:

| Index | Type          | Description                                               | Required? | Default value |
|:-----:|:-------------:|-----------------------------------------------------------|-----------|---------------|
| 0     | String        | Additional Request Parameters in HTTP Query String format | No        |               |

Example(s):

```
{{hst:componentRenderingURL}}
```

### hst:contributeHeadElement

Synopsis: Contribute an HEAD element.

| Index | Type          | Description                                               | Required? | Default value |
|:-----:|:-------------:|-----------------------------------------------------------|-----------|---------------|
| 0     | String        | Head Element content in text                              | Yes       |               |
| 1     | String        | Key hint of this contributed head element                 | No        |               |
| 2     | String        | Head Element category name                                | No        |               |

Example(s):

```
{{hst:contributeHeadElement "<meta name=\"mymeta\" content=\"Handlebars templatingis working!\"/>"}}
```

### hst:contributedHeadElements

Synopsis: Write all the contributed HEAD elements meeting the conditions.

| Index | Type          | Description                                               | Required? | Default value |
|:-----:|:-------------:|-----------------------------------------------------------|-----------|---------------|
| 0     | String        | Comma separated category names to include                 | No        |               |
| 1     | String        | Comma separated category names to exclude                 | No        |               |
| 2     | boolean       | Whether to write in XHTML format                          | No        | false         |

Example(s):

```
{{hst:contributedHeadElements "body, head", "exclude,stuff", true}}

```

### hst:setBundle

Synopsis: Set the i18n ResourceBundle in the template

| Index | Type           | Description                                               | Required? | Default value |
|:-----:|:--------------:|-----------------------------------------------------------|-----------|---------------|
| 0     | String         | Resource Bundle basename                                  | Yes       |               |
| 0     | boolean        | Whether to fall back to the Java system ResourceBundle    | No        | false         |

Example(s):

```
{{hst:setBundle "essentials.pagination"}}
```

### hst:bundleMessage

Synopsis: Get the i18n message by the given key from the underlying ResourceBundle

| Index | Type           | Description                                               | Required? | Default value |
|:-----:|:--------------:|-----------------------------------------------------------|-----------|---------------|
| 0     | String         | Resource Bundle Message key                               | Yes       |               |

Example(s):

```
{{hst:bundleMessage "results.indication"}}
```

### hst:replaceByBundle

Synopsis: Replace text by the given or underlying ResourceBundle.

| Index | Type           | Description                                               | Required? | Default value |
|:-----:|:--------------:|-----------------------------------------------------------|-----------|---------------|
| 0     | String         | String to replace                                         | Yes       |               |
| 1     | ResourceBundle | The ResourceBundle contains key-value pairs               | No        |               |

Example(s):

```
{{hst:replaceByBundleName "Hello, ${results.indication}!" bundle}}
```

### hst:replaceByBundleName

Synopsis: Replace text by the ResourceBundle that is resolved by the given bundle name.

| Index | Type           | Description                                               | Required? | Default value |
|:-----:|:--------------:|-----------------------------------------------------------|-----------|---------------|
| 0     | String         | String to replace                                         | Yes       |               |
| 1     | String         | The basename of the ResourceBundle                        | No        |               |

Example(s):

```
{{hst:replaceByBundleName "Hello, ${results.indication}!" "essentials.pagination"}}
```

### hst:manageContent

Synopsis: Create an HTML comment snippet that contains information about the manage content link in CMS.

| Index | Type           | Description                                                       | Required? | Default value |
|:-----:|:--------------:|-------------------------------------------------------------------|-----------|---------------|
| 0     | HippoBean      | Content bean to manage                                            | Yes       |               |
| 1     | String         | Path to the root folder of selectable document locations.         | No        | false         |
| 2     | String         | Initial location of a new document, relative to the rootPath.     | No        | false         |
| 3     | String         | Name of the component parameter to set when changing the content. | No        | false         |
| 4     | String         | Template query to use for creating new documents.                 | No        | false         |

Example(s):

```
{{hst:manageContent document}}
```

### hst:cmsEditMenuLink

Synopsis: Generate the link that can be used as editable link or HTML comment for a menu in CMS.

| Index | Type           | Description                                               | Required? | Default value |
|:-----:|:--------------:|-----------------------------------------------------------|-----------|---------------|
| 0     | CommonMenu     | menu object                                               | Yes       |               |

Example(s):

```
{{hst:cmsEditMenuLink menu}}
```

### hst:cmsEditLink

Synopsis: Generate the link that can be used as editable link or HTML comment for a document in CMS.

| Index | Type           | Description                                               | Required? | Default value |
|:-----:|:--------------:|-----------------------------------------------------------|-----------|---------------|
| 0     | HippoBean      | Content bean to edit                                      | Yes       |               |
| 1     | boolean        | Whether to generate as HTML comment                       | No        | false         |

Example(s):

```
{{hst:cmsEditLink document}}
```

