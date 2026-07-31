<!--
SPDX-FileCopyrightText: The yosql Authors
SPDX-License-Identifier: 0BSD
-->

# YoSQL [![Chat](https://img.shields.io/badge/matrix-%23talk.metio:matrix.org-brightgreen.svg?style=social&label=Matrix)](https://matrix.to/#/#talk.metio:matrix.org)

Take a look at the [project website](https://yosql.projects.metio.wtf/) to read the documentation.

## Development

The whole toolchain — JDK, Maven, Hugo, htmltest, Postgres — comes from `flake.nix` and is pinned in `flake.lock`.
CI runs the same shell, so a green gate here is a green gate there.

```console
nix develop --command mvn verify                        # the full gate
nix develop --command hugo server --source docs           # the website, live
nix develop .#native --command mvn -Pnative-image verify # the GraalVM native-image gate
```

## License

```text
Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH
REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM
LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR
OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
PERFORMANCE OF THIS SOFTWARE.
```
