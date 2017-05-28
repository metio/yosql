java_plugin(
  name = "jmh_generator_annprocess_plugin",
  processor_class = "org.openjdk.jmh.generators.BenchmarkProcessor",
  deps = [
    "@org_openjdk_jmh_jmh_generator_annprocess//jar",
    "@org_openjdk_jmh_jmh_core//jar",
  ],
)

java_library(
  name = "jmh",
  exported_plugins = [":jmh_generator_annprocess_plugin"],
  exports = [
    "@org_openjdk_jmh_jmh_generator_annprocess//jar",
    "@org_openjdk_jmh_jmh_core//jar",
  ],
)

java_binary(
  name = "yosql-micro-benchmarks",
  main_class = "org.openjdk.jmh.Main",
  srcs = glob(["src/main/java/**/*.java"]),
  resources = glob(["src/main/resources/**/*.sql"]),
  visibility = ["//visibility:public"],
  deps = [
    "@org_apache_commons_commons_math3//jar",
    "@net_sf_jopt_simple//jar",
    "//yosql-core",
    "//yosql-core:dagger2",
    ":jmh",
  ],
)
