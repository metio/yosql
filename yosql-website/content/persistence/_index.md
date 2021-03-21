---
title: Persistence APIs
date: 2020-04-13
menu:
  main:
    weight: 120
---

`YoSQL` supports multiple persistence APIs to interact with a database. We recommend that you pick the one that is already available in your project. In case you are starting fresh, `YoSQL` will default to the JDBC implementation because it requires no external dependencies and thus your project should be able to compile the generated code just fine.

Take a look at the available [configuration](../configuration/) options to adapt the generated code according to your needs. In order to monitor execution of your SQL statements, we recommend enabling one of the supported [logging](../logging/) APIs to add log output to the generated code.

Which of the available persistence APIs you are going to use in your project is entirely your choice, however using an API that is already used in your project is recommended to keep the number of dependencies as low as possible (zero).

TODO: link to benchmarks
