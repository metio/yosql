<!--
SPDX-FileCopyrightText: The yosql Authors
SPDX-License-Identifier: 0BSD
-->

# YoSQL

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

[0BSD](LICENSES/0BSD.txt).
