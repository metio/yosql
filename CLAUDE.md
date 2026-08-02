<!--
SPDX-FileCopyrightText: The yosql Authors
SPDX-License-Identifier: 0BSD
-->

# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

`YoSQL` turns `.sql` files into Java repositories at build time. It is a code generator: almost
everything here exists to produce Java source that somebody else compiles.

## The toolchain is the flake

Every tool — JDK, Maven, Hugo, htmltest, Postgres, GraalVM — comes from `flake.nix`, pinned in
`flake.lock`. CI runs the same shell, so a green gate locally is green in CI by construction. Always
go through it:

```console
nix develop --command mvn verify                   # the full gate, 33 modules
nix develop --command mvn --projects yosql-codegen test
nix develop --command mvn --projects yosql-codegen test --define test=JavaSourceParserTest
nix develop --command mvn --projects yosql-codegen test --define test=JavaSourceParserTest#findsRecord
nix develop --command hugo server --source docs    # the website, live
```

Lint gates come from the shared devShell and each is its own CI job:
`ci-reuse`, `ci-typos`, `ci-yaml`, `ci-markdown`.

The `.#native` shell carries GraalVM and is the only one that can build an image:

```console
nix develop .#native --command mvn --projects yosql-tooling/yosql-tooling-cli --also-make \
  --define skipNativeBuild=false verify
```

Java 25 is the baseline for the build, the bytecode and the generated code. `maven.compiler.proc=full`
is set in the root POM because JDK 23 stopped running annotation processors found on the class path
by default, and says nothing when it skips them — without it the Immutables and Dagger types are
simply absent and the first symptom is a compile error about a class nobody wrote.

## The meta-model generates the frontends

This is the part that is not visible from any single file. `yosql-models-meta` describes every
configuration setting as data. The `yosql-models-generator` Maven plugin then emits, from that one
description:

| `<type>` | consumer | what it produces |
| --- | --- | --- |
| `immutables` | `yosql-models-immutables` | the `*Configuration` interfaces the generator reads |
| `maven` | `yosql-tooling-maven` | mojo parameters |
| `gradle` | `yosql-tooling-gradle/buildSrc` | the Gradle DSL extension |
| `ant` | `yosql-tooling-ant` | task attributes |
| `cli` | `yosql-tooling-cli` | picocli options |
| `website` | `docs` | `docs/content/configuration/**` |

**So a new setting is added to `yosql-models-meta` and nowhere else.** Editing a frontend by hand
means editing generated output. The website's configuration pages are generated too and gitignored —
`docs` cleans and regenerates them, but `excludeDefaultDirectories` means its `target/` is *not*
cleaned, so a removed setting can leave a stale page behind locally.

## How generation runs

`DefaultYoSQL` in `yosql-codegen/.../orchestration` is the pipeline: parse files, validate, generate,
write. Everything is wired by Dagger in `yosql-tooling-dagger`, which every frontend depends on.

Within `yosql-codegen`:

- **`files`** — reads `.sql` files and their YAML front matter into `SqlConfiguration`, then a chain
  of *configurers* fills in whatever the user left out (method names, parameters, the converter).
- **`dao`** — the generators that emit repositories, methods and JDBC blocks as JavaPoet specs.
- **`records`** — reads the user's *Java source* with JavaParser to build converters from records,
  and to find a hand-written converter's method. Name resolution is ours, not JavaParser's, because
  no classpath exists yet — see `JavaSourceParser.Scope`.
- **`blocks`** — the small pieces (javadoc, annotations, control flow) the generators compose.

Two invariants worth knowing before changing any of it:

**Generated code must be reflection-free.** That is the reason to prefer this to an ORM, and the
`native` CI job compiles a generated repository into a `--no-fallback` image and runs it against a
real Postgres to prove it. Nothing in generated output may resolve a name at runtime.

**Errors belong at build time.** A statement that cannot be generated correctly fails the build with
a message naming the file and the statement; it never produces code that breaks for the user later.
The `exceptions` package is full of these and they are all thrown, not logged.

## Tests are mostly snapshots

`yosql-codegen` holds ~660 of the ~930 tests. The generator tests use a TCK base class
(`ReadMethodGeneratorTCK` and friends) with expectations as text blocks in the concrete subclass, so
**any change to generated output means updating expectation strings**. When several fail at once the
quickest route is to read the actual value out of `target/surefire-reports/TEST-*.xml` — the failure
message holds both sides — rather than hand-editing.

Watch two traps there: the expectations contain text blocks, so `"""` inside them needs escaping as
`""\"`; and the surefire failure message is followed by a stack trace, so cut at its first
`\n\tat` line before parsing the actual value out of it.

## Frontends the Maven reactor does not build

`yosql-tooling-gradle` and `yosql-examples-gradle` are Gradle builds. `mvn verify` never compiles
them, so a change to the generated configuration model can pass every Maven gate and still break the
Gradle plugin. Run it after touching the meta-model:

```console
nix develop --command mvn --projects yosql-tooling/yosql-tooling-dagger,yosql-examples/yosql-examples-common \
  --also-make --define maven.test.skip=true install
cd yosql-examples/yosql-examples-gradle && nix develop ../.. --command ./gradlew build run
```

## Native image configuration

The CLI ships as a native binary. What a closed-world image drops is whatever is reached *by name*
rather than by call, so `yosql-tooling-cli/src/main/resources/META-INF/native-image/` registers the
cal10n message bundles behind every diagnostic and the Jackson-bound configuration model. That list
is enumerated from the compiled classes of `yosql-models-*`; a new model class Jackson needs will not
be in it, and the failure appears only in the native binary. The native CI job generates real code
with the binary rather than running `--help`, which is what catches that.

## Conventions

Releases are calendar-versioned on the 8th of each month (`2026.8.8`) by `.github/workflows/release.yml`.
A release needing operator action gets an entry in `docs/content/installation/upgrading.md` and a
Conventional `feat!:` or `BREAKING CHANGE:` commit so git-cliff surfaces it.

Every file needs an SPDX header; formats that cannot carry comments are covered by globs in
`REUSE.toml`. The licence is 0BSD.
