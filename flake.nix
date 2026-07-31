# SPDX-FileCopyrightText: The yosql Authors
# SPDX-License-Identifier: CC0-1.0

# The single source of the development toolchain: CI and local shells run every
# gate through this flake's devShell, so both use the exact tool versions pinned
# in flake.lock. The shared lint gate and the org-wide nixpkgs pin come from
# metio/nix-devshell; Renovate keeps the lock fresh. The build itself runs via
# the shared metio/ci maven.yml, which invokes `nix develop --command mvn` — so
# a local `nix develop --command mvn verify` reproduces the gate exactly.
{
  description = "YoSQL development environment";

  inputs = {
    devshell.url = "github:metio/nix-devshell";
    nixpkgs.follows = "devshell/nixpkgs";
    flake-compat.follows = "devshell/flake-compat";
  };

  outputs =
    { nixpkgs, devshell, ... }:
    let
      systems = [
        "x86_64-linux"
        "aarch64-linux"
        "x86_64-darwin"
        "aarch64-darwin"
      ];
      forAllSystems = f: nixpkgs.lib.genAttrs systems (system: f nixpkgs.legacyPackages.${system});
    in
    {
      devShells = forAllSystems (
        pkgs:
        let
          # The generator emits sources for its consumers to compile, so the JDK
          # it runs on is also the oldest JDK those sources have to be valid for.
          # Consumers baseline on 17; running Maven on 17 keeps the toolchain
          # honest, because a newer JDK would happily accept generator sources
          # the parent POM's `<release>` setting is meant to reject.
          jdk = pkgs.jdk17;
          maven = pkgs.maven.override { jdk_headless = jdk; };
          # The version the native-image gate runs the generated repository
          # against. Tests start their own throwaway cluster from this binary, so
          # "works locally" and "works in CI" mean the same Postgres — and no
          # test needs a daemon, a container runtime, or the network.
          postgres = pkgs.postgresql_18;
        in
        {
          default = devshell.lib.mkDevShell {
            inherit pkgs;
            packages = [
              jdk
              maven
              # The documentation site. Building it is a gate, not a chore: a
              # broken shortcode or a dead internal link fails the same way a
              # broken test does.
              pkgs.hugo
              # Link checking for the built site.
              pkgs.htmltest
              postgres
            ];
            env.JAVA_HOME = "${jdk}";
            menu = ''
              echo "YoSQL — JDK 17 + Maven + Hugo."
              echo "  nix develop --command mvn verify              # full gate"
              echo "  nix develop --command hugo --source yosql-website"
              echo "  nix develop --command hugo server --source yosql-website"
              echo "  nix develop .#native --command mvn -Pnative-image verify"
            '';
          };

          # The native-image build needs a GraalVM JDK, whose closure is far
          # larger than the ordinary gate should pay to download. Kept as its own
          # shell so `nix develop` stays fast and CI only fetches GraalVM in the
          # jobs that actually compile an image — the CLI distribution and the
          # reflection-free proof.
          native = devshell.lib.mkDevShell {
            inherit pkgs;
            packages = [
              pkgs.graalvmPackages.graalvm-ce
              (pkgs.maven.override { jdk_headless = pkgs.graalvmPackages.graalvm-ce; })
              # The proof runs a generated repository against a real database,
              # inside a native image. Without one, the gate could only show that
              # the image links — not that a query returns rows.
              postgres
            ];
            env.JAVA_HOME = "${pkgs.graalvmPackages.graalvm-ce}";
            menu = ''
              echo "YoSQL — GraalVM native image."
              echo "  nix develop .#native --command mvn -Pnative-image verify"
            '';
          };
        }
      );

      formatter = forAllSystems (pkgs: pkgs.nixfmt-rfc-style);
    };
}
