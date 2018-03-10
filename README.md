# templating-support

This is a project to support various view templating technologies other than FreeMarker
in Hippo CMS Delivery tier web application.

This project is experimental and being implemented by volunteer members in the community, not part of official products.
Therefore please respect the volunteers when asking questions or asking for helps.

## Module Overview

This project consists of the following submodules:

- [core](core) : The core APIs, Utilities and base classes which make it easier to implement a specific templating technology specific servlet.
- [handlebars](handlebars) : View templating servlet implementations based on [core](core) submodule.
- ... (other submodules will come for other view templating technologies. e.g, Velocity, Thymeleaf, etc.)

## [core](core) submodule

Core APIs, Utilities and base classes.

Features supported at the moment:
- Common base class: org.onehippo.forge.templating.support.core.servlet.AbstractHstTemplateServlet
- Default JCR Event Listener implementation to invalidate template cache on changes, by invoking ```#clearTemplateCache()``` of the specific implemenation servlets.

TODOs:
- Add JavaDocs.
- Add Project Site documentation.
- ...

## [handlebars](handlebars) submodule

Handlebars view technology supporting servlet.

Features supported at the moment:
- Default Handlebars servlet: org.onehippo.forge.templating.support.handlebars.servlet.HandlebarsHstTemplateServlet,
  supporting WebfileTemplateLoader (```webfile:...```), ClassPathTemplateLoader (```classpath:...```), ServletContextTemplateLoader
  based on protocol prefixes.
- Demo templates in [demo](demo) folder.

TODOs:
- Implement HandlebarsHstTemplateServlet#createTemplateContext(HttpServletRequest, HttpServletResponse) properly,
  which is supposed to create a Context object from HttpServletRequest's attributes,
  as model objects are contributed by HstComponents through request attributes.
- ...

### How to install

In site/pom.xml, add the following dependency:

```xml
    <dependency>
      <groupId>org.onehippo.forge.templating-support</groupId>
      <artifactId>templating-support-handlebars</artifactId>
      <version>${forge.templating-support.version}</version>
    </dependency>

    <dependency>
      <groupId>com.github.jknack</groupId>
      <artifactId>handlebars</artifactId>
      <version>${handlebars.version}</version>
    </dependency>
```

In site/src/main/webapp/WEB-INF/web.xml, add the following:

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

Add ```*.hbs``` in "Included Files" in webfiles configuration. See [https://www.onehippo.org/library/concepts/web-files/web-files-configuration.html](https://www.onehippo.org/library/concepts/web-files/web-files-configuration.html).

## Demo Application

You can build the module locally (as it's not released yet) first in the project root folder.

```bash
$ mvn clean install
```

And you can build and run the [demo](demo) project:

```bash
$ cd demo
$ mvn clean verify && mvn -Pcargo.run
```

Visit http://localhost:8080/site/.

You will see "Hello anonymous from base-footer.hbs!" at the bottom,
which is rendered by the template configured at ```/hst:hst/hst:configurations/templatingsupportdemo/hst:templates/base-footer``` like the following in the demo project:


```
/base-footer:
  jcr:primaryType: hst:template
  hst:renderpath: webfile:/hbs/templatingsupportdemo/base-footer.hbs
```

If you update the base-footer.hbs file locally in your project, it will be updated automatically as well.


## For Contributors

- Please ask and discuss if you want to collaborate, contribute or review on anything through Hippo Community Forum.
- Specific view templating dependencies should be added as ```optional``` and end projects should include the dependency explicitly.
- Once we get a relatively stable version, we'll cut the initial release, 0.1.0, with online site documentations including release notes.
- All the contributors are encouraged to *fork* and create *pull requests* for any contributions.
- When submitting a pull request, please make sure to add proper license headers. See existing sources.

