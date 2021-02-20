---
title: basePackageName
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

The base package name for the generated code. Defaults to `com.example.persistence`.

## Configuration Options

### Option: 'com.example.persistence'

The default value of the `basePackageName` configuration option is `com.example.persistence`. Setting the option to `com.example.persistence` therefore produces the same code generated as the default configuration.

```java
package com.example.persistence;

public class SomeRepository {

    // ... rest of generated code

}
```

### Option: 'your.own.domain'

Changing the `basePackageName` configuration option to `your.own.domain` generates the following code instead:

```java
package your.own.domain;

public class SomeRepository {

    // ... rest of generated code (same as above)

}
```
