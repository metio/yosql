---
title: Front matter schema
date: 2026-08-02
menu:
  main:
    parent: Editors
categories:
  - Editors
tags:
  - schema
  - intellij
  - vscode
---

The front matter of a statement is YAML written inside SQL comments, which means your editor
highlights the SQL and leaves the part with all the keys in it entirely unassisted. A JSON schema
fixes that: completion for every key, the description on hover, and a warning on a key that does not
exist.

The schema is published at
<https://yosql.projects.metio.wtf/schema/frontmatter.json> and generated from the same description of
the settings that generates the [configuration reference](../../configuration/), so the two cannot
drift.

## What it checks

Every key a statement accepts, its description, and the type of the ones that take a scalar —
`returning` is one of four words, `executeBatch` is a boolean, `repository` is a string. Keys that
accept more than one shape, such as `parameters` and `resultRowConverter`, are listed without a type
rather than described half-correctly, so completion offers them and then stays out of your way.

A key that is not in the schema is flagged. That is deliberate: an unknown key in the front matter
is a typo, and finding it while typing beats finding it in a build log.

## IntelliJ IDEA

**Settings → Languages & Frameworks → Schemas and DTDs → JSON Schema Mappings**, add a mapping:

- **Schema file or URL**: `https://yosql.projects.metio.wtf/schema/frontmatter.json`
- **Schema version**: JSON Schema version 7 or later
- **File path pattern**: `src/main/yosql/**/*.sql`

IDEA applies JSON schemas to YAML, but not to YAML embedded in SQL comments. Where you want the
checking while writing a new statement, draft the front matter in a scratch `.yaml` file mapped to
the schema and paste it in with the `--` prefixes. It is a workaround; it is also the difference
between finding a typo now and finding it in a build.

## Visual Studio Code

With the [YAML extension](https://marketplace.visualstudio.com/items?itemName=redhat.vscode-yaml),
add to `.vscode/settings.json`:

```json
{
  "yaml.schemas": {
    "https://yosql.projects.metio.wtf/schema/frontmatter.json": [
      "**/yosql/**/*.yaml"
    ]
  }
}
```

The same caveat applies — the mapping is for YAML files, not for YAML inside SQL comments.

## Using it in a check of your own

The schema is an ordinary JSON Schema document, so anything that speaks the format can use it. A
pre-commit hook that strips the `--` prefixes and validates the result catches a mistyped key before
the build does:

```shell
curl -sO https://yosql.projects.metio.wtf/schema/frontmatter.json
```

Pin the copy rather than fetching it every run — the schema grows as settings are added, and a check
that changes under you is worse than no check.
