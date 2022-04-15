---
title: {{group.name}}
date: {{currentDate}}
menu:
  main:
    parent: Configuration
categories:
  - Configuration
tags:
  - {{#lower}}{{group.name}}{{/lower}}
{{#group.tags}}
  - {{.}}
{{/group.tags}}
generated: true
---

{{group.description}}
