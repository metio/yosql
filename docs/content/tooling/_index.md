---
title: Tooling
date: 2020-04-13
menu:
  main:
    weight: 100
---

`YoSQL` is available as a build tool which needs to be integrated into your project. Each currently supported tool has its own documentation page, which is listed below. We recommend using the tooling that is most appropriate for your project, e.g. [Maven](https://maven.apache.org/) projects should use the `YoSQL` Maven tooling. Since `YoSQL` is not required at run-time, you could use it just once to generate code and not add `YoSQL` to your build at all.

In its default configuration, `YoSQL` will try to generate as much code as possible, and you are expected to configure the generated code so that it matches the requirements of your project. Take a look at the [configuration](/configuration/) section in order to see all available configuration options in case you want to change something.

Once your initial setup is complete, it's time to write [SQL](/sql/) statements. There is no upper limit imposed by `YoSQL` on how many statements there can be per generated repository, however Java has a hard limit of [65535 methods per class](https://docs.oracle.com/javase/specs/jvms/se18/html/jvms-4.html#jvms-4.11). We recommend splitting your statements according to the domain objects in your projects, e.g. all statements that are related to a single class should go in one repository, but you are free to choose whatever structure best fits your project.

Using any of the listed tools below allows you to convert SQL statements into runnable Java code that no longer requires the original SQL statements.
