<!--
SPDX-FileCopyrightText: The yosql Authors
SPDX-License-Identifier: 0BSD
-->

# Signatures

A release produces two kinds of artifact and each carries the signature its
channel can actually check.

## Maven Central: PGP

Every file published to Maven Central is accompanied by a detached PGP
signature. This is not a choice — Central rejects a deployment whose files are
not signed, so a Maven consumer either gets PGP or gets nothing.

The signing is done by [sign-maven-plugin][sign], configured in the parent POM
and activated by the `release` profile. The release workflow hands it the key
and passphrase as `sign.keyFile` and `sign.keyPass`; the key itself lives in the
`GPG_SECRET_KEY_BASE64` repository secret and is written to the runner's
temporary directory, never into the repository or the settings file.

To verify a downloaded artifact:

```console
gpg --verify yosql-tooling-maven-<version>.jar.asc yosql-tooling-maven-<version>.jar
```

## GitHub release archives: cosign, keyless

The Ant task and the CLI are downloaded, not resolved, and nothing about that
channel requires PGP. They are covered by a `SHA256SUMS` manifest signed with
[cosign][cosign] in keyless mode: the release workflow requests an OIDC token
from GitHub, Fulcio issues a short-lived certificate bound to that workflow
identity, and the signature plus certificate plus the Rekor transparency-log
proof are written to `SHA256SUMS.bundle`.

One signed manifest rather than one signature per archive: the manifest names
every archive and its digest, so a single verification covers the whole release
and an archive added later cannot quietly escape it.

To verify a release:

```console
cosign verify-blob SHA256SUMS \
    --bundle SHA256SUMS.bundle \
    --certificate-identity-regexp '^https://github\.com/metio/yosql/\.github/workflows/release\.yml@refs/' \
    --certificate-oidc-issuer 'https://token.actions.githubusercontent.com'
sha256sum --check SHA256SUMS
```

The certificate identity is the point of the exercise: it proves the archives
came out of this repository's release workflow, which a bare digest cannot —
a digest is equally unforgeable for a file somebody else published.

## Why not one or the other

A keyless signature has no long-lived private key to protect and binds the
artifact to the workflow that produced it, which is the property a consumer
actually wants to check. Central does not accept it. A PGP key proves control
of the key and nothing about where the build ran. Using each where it is
accepted is the only arrangement that leaves nothing unsigned.

[sign]: https://www.simplify4u.org/sign-maven-plugin/
[cosign]: https://docs.sigstore.dev/cosign/signing/signing_with_blobs/
