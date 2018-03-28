
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

### Generating HST Link like *<@hst.link />* tag

You can generate an HST link with a ```HippoBean``` like the following:

```
{{hst:linkByHippoBean document}}
```

To generate an absolute link, append ```true``` parameter (it's ```false``` by default):

```
{{hst:linkByHippoBean document true}}
```

You can generate an HST link with a path info like the following:

```
{{hst:linkByHippoBean "/news"}}
```

To generate an absolute link, append ```true``` parameter (it's ```false``` by default):

You can generate an HST link with an HST SiteMap Item Reference like the following:

```
{{hst:linkBySiteMapItemRefId "root"}}
```

To generate an absolute link, append ```true``` parameter (it's ```false``` by default):


### Generating HST WebFile Link like *<@hst.webfile />* tag
```
{{hst:webfileByPath "/js/bootstrap.min.js"}}
```


### Rendering Hippo HTML Compound Bean like *<@hst.html />* tag

```
   {{{hst:htmlByHippoHtml document.content}}}
```

### Including Child HST Components like *<@hst.include />* tag

```    
    {{{hst:includeChild "container"}}}
 
```

### Generating HST Render URLs like *<@hst.renderURL />* tag
```
{{hst:renderURL}}
{{hst:renderURL "a=1&b=2"}}
```
### Generating HST Action URLs like *<@hst.actionURL />* tag

```
{{hst:actionURL}}
```

### Generating HST Resource URLs like *<@hst.resourceURL />* tag

```
{{hst:resourceURL "aResourceID"}}
```

### Generating HST ComponentRendering URLs like *<@hst.componentRenderingURL />* tag
```
{{hst:componentRenderingURL}}
```
### Contributing HST Head Element like *<@hst.headContribution />* tag
```
{{hst:contributeHeadElement "<meta name=\"mymeta\" content=\"Handlebars templatingis working!\"/>"}}
```
### Writing All the Contributed HST Head Elements like *<@hst.headContributions />* tag
```
{{contributedHeadElements "body, head", "exclude,stuff", true}}

```
Third parameter indicates xhml (if true)

### Message Replacing like *<@hst.messagesReplace />* tag
```
{{replaceByBundle "test" bundle}}
{{replaceByBundleName "test" "name"}}
```
###  <@hst.manageContent /> tag
```
{{hst:manageContent document}}
```
###  <@hst.cmsEditLink /> tag
```
{{hst:cmsEditLink document}}
```
as text:
```
{{hst:cmsEditLink document true}} 
```
###  <@hst.facetNavigationLink /> tag
```
 {{hst:linkForFacet facetBean facetBean}}
```
###  <@hst.setBundle /> tag
```
{{hst:setBundle "essentials.pagination"}} 
```

###  <@fmt.message /> tag
```
{{hst:bundleMessage "results.indication"}}
```