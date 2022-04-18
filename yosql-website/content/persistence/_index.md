---
title: Persistence APIs
date: 2020-04-13
menu:
  main:
    weight: 140
---

`YoSQL` supports multiple persistence APIs to interact with a database. We recommend that you pick the one that is already available in your project. In case you are starting fresh, `YoSQL` will default to the JDBC implementation because it requires no external dependencies and thus your project should be able to compile the generated code just fine. Some `YoSQL` tooling like the Maven plugin might auto-detect certain settings in your project to make your life easier, however you are always in full control and can change very aspect of the generated code.

Take a look at the available [configuration](/configuration/) options to adapt the generated code according to your needs. In order to monitor execution of your SQL statements, we recommend enabling one of the supported [logging](/logging/) APIs to add log output to the generated code. Read the [SQL](/sql/) section to learn how to write SQL statements in a `YoSQL`-enabled project.
