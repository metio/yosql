with import <nixpkgs> {};

mkShell {
  buildInputs = [ git openjdk11 cmake perl bazel ncurses silver-searcher entr ];
}
