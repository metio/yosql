java_plugin(
  name = "jmh_generator_annprocess_plugin",
  processor_class = "org.openjdk.jmh.generators.BenchmarkProcessor",
  deps = [
    "//external:jmh_generator_annprocess",
    "//external:jmh_core",
  ],
)

java_library(
  name = "jmh",
  exported_plugins = [":jmh_generator_annprocess_plugin"],
  exports = [
    "//external:jmh_generator_annprocess",
    "//external:jmh_core",
  ],
)

java_binary(
  name = "yosql-jmh-benchmark",
  main_class = "org.openjdk.jmh.Main",
  srcs = glob(["src/main/java/**/*.java"]),
  resources = glob(["src/main/resources/**/*.sql"]),
  visibility = ["//visibility:public"],
  deps = [
    "//external:commons_math3",
    "//external:jopt_simple",
    "//yosql-core",
    ":jmh",
  ],
)
