---
title: {{setting.name}}
date: {{currentDate}}
menu:
  main:
    identifier: {{#lower}}{{group.name}}{{/lower}}-{{setting.name}}
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

{{#hasExamples}}
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
{{/hasExamples}}
{{#hasRelatedSettings}}

## Related Options

Also in this group: {{{relatedSettingsLine}}}.
{{/hasRelatedSettings}}

## Front Matter

In order to configure this option, place the following code in the front matter of your SQL statement:

```sql
-- {{setting.name}}: {{frontMatterExampleCode}}
SELECT  something
FROM    your_database_schema
WHERE   some_column = :some_value
```
