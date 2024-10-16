[//]: # (  Copyright 2018-2024 Bloomreach B.V. (https://www.bloomreach.com)
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

## Install Handlebars Templating Support Module

### Bloomreach Forge Maven Repository

Make sure you have the following **repository** configuration under the **repositories** section in the root ```pom.xml```.

```xml
    <repository>
      <id>bloomreach-maven2-forge</id>
      <name>Bloomreach Maven 2 Repository</name>
      <url>https://maven.bloomreach.com/repository/maven2-forge/</url>
    </repository>
```

### Adding Dependencies

In the root ```pom.xml```, add a version property (see [release notes](release-notes.html) for the latest version):

```xml
    <properties>
      <forge.templating-support.version>4.0.0</forge.templating-support.version>
    </properties>
```
For a project on 13, add the following dependency in ```cms-dependencies/pom.xml```, :

```xml
    <dependency>
      <groupId>org.onehippo.forge.templating-support</groupId>
      <artifactId>templating-support-core-platform</artifactId>
      <version>${forge.templating-support.version}</version>
    </dependency>
```

Add the following dependency in ```site/pom.xml```:

```xml
    <dependency>
      <groupId>org.onehippo.forge.templating-support</groupId>
      <artifactId>templating-support-handlebars</artifactId>
      <version>${forge.templating-support.version}</version>
    </dependency>
```

### Adding Servlet Configuration

In ```site/src/main/webapp/WEB-INF/web.xml```, add the following:

```xml
  <servlet>
    <servlet-name>handlebars</servlet-name>
    <servlet-class>org.onehippo.forge.templating.support.handlebars.servlet.HandlebarsHstTemplateServlet</servlet-class>
    <init-param>
      <param-name>cache.enabled</param-name>
      <param-value>true</param-value>
    </init-param>
    <load-on-startup>200</load-on-startup>
  </servlet>

  <-- SNIP -->

  <servlet-mapping>
    <servlet-name>handlebars</servlet-name>
    <url-pattern>*.hbs</url-pattern>
  </servlet-mapping>
```

### Configure WebFiles to Include **.hbs** File Extension

Add ```*.hbs``` in **Included Files** in webfiles configuration. See [https://xmdocumentation.bloomreach.com/library/concepts/web-files/web-files-configuration.html](https://xmdocumentation.bloomreach.com/library/concepts/web-files/web-files-configuration.html).

### Switch Template Support

By default, the Switch Template Support feature in the product supports only FreeMarker templates. Follow the instruction in the [Switch Template Support section](index.html#Switch_Template_Support) to enable the feature for Handlebars in your project.
