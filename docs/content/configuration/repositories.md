---
title: Repositories
date: 2019-09-27T18:51:08+02:00
menu:
  main:
    parent: Configuration
categories:
  - Configuration
tags:
  - repositories
  - basePackageName
---

# `basePackageName`

The base package name for the generated code. Defaults to `com.example.persistence`.

```java
package com.example.persistence;

public class SomeRepository {

    // ... rest of generaed code

}
```

Changing the `basePackageName` configuration option to `your.own.domain` generates the following code instead:

```java
package your.own.domain;

public class SomeRepository {

    // ... rest of generated code (same as above)

}
```
