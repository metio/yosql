---
title: Ant
date: 2019-06-16T18:23:45+02:00
menu:
  main:
    parent: Tooling
categories:
  - Tooling
tags:
  - Ant
---

In order to use the `YoSQL` tooling for [Ant](https://ant.apache.org/), follow these steps:

1. Download the `yosql-tooling-ant` task zip file from the [latest release](https://github.com/metio/yosql/releases/latest) (or any prior version).
2. Define a task in your build.xml. The `lib` folder of the `yosql-tooling-ant` zip file contains all jar files that are required for the task.
3. Write `.sql` files in a directory of your choice (e.g. `/path/to/your/sql/files`).
4. Adjust the [configuration](/configuration/) of the `YoSQL` task.
5. Execute the `YoSQL` task in order to generate Java code.

An example build.xml file could look like this:

```xml
<project name="YourProject">
  <description>
    example showing how to use YoSQL with Ant
  </description>

  <!-- define YoSQL task -->
  <taskdef name="yosql"
           classname="wtf.metio.yosql.tooling.ant.YoSQLGenerateTask"
           classpath="/path/of/all/yosql/jar/files"/>

  <!-- configure YoSQL -->
  <yosql>
    <files inputBaseDirectory="/path/to/your/sql/files"
           outputBaseDirectory="/path/for/writing/java/code"/>
    <repositories generateInterfaces="true"/>
    <converter>
      <rowConverters alias="yourConverter"
                     converterType="com.example.YourConverter"
                     methodName="yourCustomMethod"
                     resultType="com.example.YourDomainObject"/>
    </converter>
  </yosql>
</project>
```
