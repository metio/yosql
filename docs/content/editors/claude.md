---
title: Claude skill
date: 2026-08-02
menu:
  main:
    parent: Editors
categories:
  - Editors
tags:
  - claude
  - agents
---

[Claude Code](https://claude.com/claude-code) writing a statement for you needs to know things that
are not visible in the file it is editing: that a result row type is read from *source* rather than
from the classpath, that a name matching no configured prefix generates nothing at all, that a
parameter with no type fails the build rather than becoming an `Object`. A skill tells it.

## Installing it

Copy the skill into your own project:

```shell
mkdir -p .claude/skills/yosql
curl -o .claude/skills/yosql/SKILL.md https://yosql.projects.metio.wtf/claude/SKILL.md
```

Commit it, and every contributor gets the same behaviour. Claude loads it when it notices `.sql`
files under a `YoSQL` source directory, a `yosql-tooling-*` plugin in the build, or a `yosql.args`
file — you do not have to mention it.

Put it in `~/.claude/skills/yosql/SKILL.md` instead to have it available in every project on your
machine.

## What it knows

- Where statements and records have to live, and how the directory a `.sql` file sits in decides the
  repository it generates into.
- The front matter keys that matter, and which ones are usually inferred rather than written.
- That a statement's kind comes from its name prefix, and that a name matching none of them silently
  produces no code — the mistake most likely to waste an afternoon.
- How parameter types are found: from the front matter, from the component of the same name on the
  result row type, or from the column the parameter is named after. Including the short names, so it
  writes `uuid` rather than `java.util.UUID`.
- When to let the schema write the result record instead of writing one, and when a record has to be
  written by hand because it carries more than columns.
- That a collection parameter becomes an `in (...)` list, and which names a parameter cannot have.
- That a licence header belongs in a block comment, because `--` lines at the top of a file are
  front matter.
- How a record maps to a result row, when to alias a column in the query instead of reaching for
  [resultRowColumns](../../configuration/sql/resultrowcolumns/), and what makes a build fail.
- Running several statements in one transaction.
- What not to do — chiefly, editing generated code.

## Keeping it honest

The file served above is the one
[in the repository](https://github.com/metio/yosql/blob/main/.claude/skills/yosql/SKILL.md), copied
in when the site is built, so the two cannot disagree. It changes with the generator, so re-fetch it
when you upgrade. It describes behaviour, not
version numbers; if you find it saying something that is no longer true,
[open an issue](https://github.com/metio/yosql/issues/new) — a skill that lies is worse than no
skill.

## Other agents

The file is ordinary Markdown with a small front matter header. Tools that read `AGENTS.md`, Cursor
rules or a similar convention can use the same content — drop the body into whatever file your tool
reads.
