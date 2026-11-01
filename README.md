<!--
SPDX-FileCopyrightText: The yosql Authors
SPDX-License-Identifier: 0BSD
-->

# YoSQL

Take a look at the [project website](https://yosql.projects.metio.wtf/) to read the documentation.

## Development

The whole toolchain — JDK, Maven, Hugo, htmltest, Postgres — comes from [breto](https://github.com/metio/breto),
which installs what [`.breto.edn`](.breto.edn) names in the versions the
[metio/toolchains](https://github.com/metio/toolchains) groups pin. CI installs the same versions, so a green gate
here is a green gate there.

```console
breto run mvn verify                                    # the full gate
breto run hugo server --source docs                     # the website, live
```

GraalVM comes from the environment of `yosql-examples/yosql-examples-native`, which the native-image gate uses:

```console
eval "$(breto env --dir yosql-examples/yosql-examples-native)"
mvn -Pnative-image verify
```

## License

[0BSD](LICENSES/0BSD.txt).
