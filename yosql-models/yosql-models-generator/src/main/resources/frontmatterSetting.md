---
title: {{setting.name}}
date: {{currentDate}}
menu:
  main:
    parent: {{group.name}}
categories:
  - Configuration
tags:
  - {{#lower}}{{group.name}}{{/lower}}
{{#setting.tags}}
  - {{.}}
{{/setting.tags}}
generated: true
---

{{setting.description}}

{{#hasExplanation}}
{{setting.explanation}}

{{/hasExplanation}}
## Configuration Options

{{#setting.examples}}
### Option: '{{value}}'

{{description}}

{{#result}}
```java
{{{.}}}
```

{{/result}}
{{/setting.examples}}
{{#hasRelatedSettings}}
## Related Options

{{#relatedSettings}}
- [{{name}}](../{{#lower}}{{name}}{{/lower}}/): {{description}}
{{/relatedSettings}}
{{/hasRelatedSettings}}

## Front Matter

In order to configure this option, place the following code in the front matter of your SQL statement:

```sql
-- {{setting.name}}: configValue
SELECT  something
FROM    your_database_schema
WHERE   some_column = :some_value
```
