
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

TODO

### Generating HST WebFile Link like *<@hst.webfile />* tag

TODO

### Rendering Hippo HTML Compound Bean like *<@hst.html />* tag

TODO

### Including Child HST Components like *<@hst.include />* tag

TODO

### Generating HST Render URLs like *<@hst.renderURL />* tag

TODO

### Generating HST Action URLs like *<@hst.actionURL />* tag

TODO

### Generating HST Resource URLs like *<@hst.resourceURL />* tag

TODO

### Generating HST ComponentRendering URLs like *<@hst.componentRenderingURL />* tag

TODO

### Contributing HST Head Element like *<@hst.headContribution />* tag

TODO

### Writing All the Contributed HST Head Elements like *<@hst.headContributions />* tag

TODO

### Message Replacing like *<@hst.messagesReplace />* tag

TODO
